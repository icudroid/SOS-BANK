STARTER.jsPackage("starter.profile");


starter.profile.ListController = function(options){
    this._init(options);
};

starter.profile.ListController.prototype = {
    options : null,
    _init : function(options){
        var that = this;
        that.options = options;

        $(".profile-delete-action,.role-delete-action").click(that._confirmAction);

        $(document).on("click",".btn-action",this._ModalAction);

        $("#profiles-table,#roles-table").DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/French.json"
            }
        });

        that._initPermissions();

    },


    _ModalAction : function(e){
        //e.preventDefault();

        STARTER.ajaxModal(e,this);
        /*$( "#profileModal").find(".modal-content").load( this.href, function(responseText, status, jqXHR) {
                if (status == "error") {
                    $("#profileModal").html(responseText);
                    $( "#profileModal").modal();
                }else{
                    $( "#profileModal").modal({backdrop: 'static'});
                }

        });*/
    },

    _confirmAction : function(e){
        e.preventDefault();
        var url = this.href;

        bootbox.confirm($(this).data("delete"), function(result) {
            if(result){
                $.ajax({
                    dataType: "json",
                    url: url,
                    success: function(data){
                        window.location.reload();
                    }
                });
            }
        });
    },


    _initPermissions : function(){
        var that = this;

        var dataTable = new starter.utils.datatable.ActionDatatable( {
            src: $("#permissions-table"),
            dataTable: {
                "ajax": function (data, fn, oSettings) {
                    STARTER.postJSON(that.options.searchPersissionUrl, data, function (data, textStatus, jqXHR) {
                        fn(data, textStatus, jqXHR);
                    });
                },
                "order": [
                    [0, "desc"]
                ],
                "columns": [
                    {
                        searchType:'text',
                        title: "Nom",
                        data: "name",
                        varName : 'permission'
                    },
                    {
                        searchType:'text',
                        title: "right",
                        data: "right"
                    },
                    {
                        searchType:'text',
                        title: "description",
                        data: "description",
                        orderable:false
                    },
                    {
                        searchType:'select',
                        options : [
                            {value:'',label:"Toutes les permissions"},
                            {value:false,label:"non raccordées"},
                            {value:true,label:"raccordées"}
                        ],
                        title: "Rôle(s) lié(s)",
                        data: "roles",
                        varName: "attachedRole",
                        orderable:false,
                        render: function (data, type, full, meta) {
                            var html = "";
                            if(data.length == 0){
                                html+="<div class='col-md-12'>";
                                html+="<input type='text' class='roles-select-chooser form-control select2'/><br/>";
                                html+="<button data-permission-id='"+full.id+"' class='btn blue pull-right role-btn-submit'>Valider</button>";
                                html+="</div>";
                            }else{
                                for (var i = 0; i < data.length; i++) {
                                    var role = data[i];
                                    var href = that.options.roleEditUrl + role.id;
                                    if(i>0){
                                        html+=", ";
                                    }

                                    html+='<a class="btn-action" href="'+href+'">'+role.name+'</a>';
                                }
                            }
                            return html;
                        }
                    },
                    {
                        searchType:'btnSearch',
                        title: "Opérations",
                        data: "id",
                        render: function (data, type, full, meta) {
                            return "";
                        },
                        orderable:false
                    },

                ]
            }
        } );

        dataTable.getDataTable().on( 'draw' , function(){
            $("input.roles-select-chooser").select2({
                placeholder: "Sélectionner...",
                allowClear: true,
                width:"100%",
                ajax: {
                    url: that.options.roleSearchByNameUrl,
                    dataType: 'json',
                    delay: 250,
                    data: function (searchTerm, pageNumber, context) {
                        return {
                            q: searchTerm, // search term
                            page: pageNumber-1,
                            size:15
                        };
                    },
                    results: function (data, pageNumber, query) {
                        return {
                            results: $.map(data.content,function(item){
                                return {
                                    text : item.name + ' ('+item.description+')',
                                    id : item.id
                                }
                            }),
                            more:!data.last
                        };
                    }
                }
            });
        });


        $(document).on('click','.role-btn-submit',function(e){
            //1) effacer message erreur
            var btn = $(this);
            var td = btn.parent();
            td.find("span.error").remove()
            //2) ajax requete
            var data = {
                permissionId : btn.data("permissionId"),
                roleId : $(td).find(".roles-select-chooser").select2('data').id
            };
            STARTER.postJSON(that.options.addRoleToPermission,data,function(data){
                if(data.resultCode==0){ //si ok refresh datatable
                    dataTable.submitFilter();
                }else{ //sinon affichage message d'erreur
                    $("<span class='error text-danger'>"+data.errorMessage+"</span>").insertAfter(btn);
                }

            })
        });

    }

};