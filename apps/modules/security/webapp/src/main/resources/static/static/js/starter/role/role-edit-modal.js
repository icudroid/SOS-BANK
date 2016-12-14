STARTER.jsPackage("starter.role");

starter.role.RoleModal = function(options){
    this._init(options);
};

starter.role.RoleModal.prototype = {

    options : null,
    datatable : null,

    _init : function(options){

        $("body").on('hide.bs.modal', function (e) {
            $.fn.dataTable.ext.search.pop();
        });

        $('[data-toggle="tooltip"]').tooltip();

        var that = this;
        this.options = options;

        if(options.security.authorize.modifiy){
            if(options.roleId != null){
                $('#rName').editable({
                    mode: 'inline',
                    type: 'text',
                    title: 'Entrer le nouveau nom du role',
                    success: function(response, newValue) {
                        $('#name').val(newValue);
                    },
                    validate: function(value) {
                        if($.trim(value) == '') {
                            return 'Le nom du role est requis.';
                        }else{

                            var result;
                            $.ajax({
                                async : false,
                                url : options.existRoleIdUrl+options.roleId+"/"+value,
                                dataType: "json",
                                success : function(data){
                                    if(data.data){
                                        result =  'Le nom du role "'+value+'" existe';
                                    }
                                }
                            });

                            return result;

                        }
                    }
                });

                $('#rDescription').editable({
                    mode: 'inline',
                    type: 'textarea',
                    title: 'Entrer la description du role',
                    success: function(response, newValue) {
                        $('#description').val(newValue);
                    }
                });
            }
            $('#btn-save-role').click(function(e){
                e.preventDefault();
                that._onSave(that);
            });
        }


        var onlySelected =  $("#selected-permission").is(':checked');

        $.fn.dataTable.ext.search.push(
            function( settings, data, dataIndex,d) {
                if(onlySelected){
                    return $(settings.aoData[dataIndex].anCells[1]).find("input[type='checkbox']").is(':checked')
                }else{
                    return true;
                }
            }
        );

        $("#selected-permission").change(function(){
            onlySelected = $(this).is(':checked');
            that.datatable.draw();
            $('body').trigger("modal-change-content")
        });


        this.datatable = $('#table-role-permissions').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/French.json"
            }
        });

    },


    _onSave : function(context){
        var $initialForm = $("#save-role-form");

        var form = $initialForm.find("input, select, textarea").serialize();
        form+="&";
        form+=context.datatable.$('input').serialize();


        $.ajax({
            type : 'POST',
            url : $initialForm.attr("action"),
            data : form,
            success : function(data){
                $( "#"+STARTER.lastActiveModal()).find(".modal-content").html(data);
                $('body').trigger("modal-change-content");
            }
        });
    }

};