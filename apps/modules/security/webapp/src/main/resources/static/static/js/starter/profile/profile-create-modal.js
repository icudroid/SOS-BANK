STARTER.jsPackage("starter.profile");

starter.profile.ProfileCreateController = function(options){
    this._init(options);
};

starter.profile.ProfileCreateController.prototype = {
    options : null,
    datatable : null,

    _init : function(options){
        var that = this;
        that.options = options;

        this.datatable = $("#table-profile-roles").DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/French.json"
            }
        });
        $("#btn-save-profile").click(function(e){
            e.preventDefault();
            that._onSubmit(that)
        });

        $(".action-show-permission").popover(
            {
                content : function(){
                    return  that._onShowPermission(this,that);
                },
                trigger : 'focus',
                html : true,
                placement : 'auto',
                template : '<div class="popover" style="max-width:600px;width: 600px;" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
            }

        ).on("shown.bs.popover",function(e){
                $("#"+$(this).attr("aria-describedby")).find("table").DataTable({
                    "language": {
                        "url": "//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/French.json"
                    }
                });
        });
    },


    _onShowPermission : function(elt,context){

        var table;
        $.ajax({
            async : false,
            dataType: "json",
            url: this.options.findPermissionByRoleIdUrl+$(elt).data("role"),
            success: function(data){
                table = '<table class="display table table-striped table-bordered table-hover">';
                table += '<thead>';
                table += '<tr>';
                table += '<th>';
                table += 'Nom de permission';
                table += '</th>';
                table += '<th>';
                table += 'Droits';
                table += '</th>';
                table += '</tr>';
                table += '</thead>';
                table += '<tbody>';


                for (var i = 0; i < data.length; i++) {
                    var permission = data[i];
                    table += '<tr>';
                    table += '<td>'+permission.permission+'</td>';
                    table += '<td>';
                    for (var j = 0; j < permission.rights.length; j++) {
                        var right = permission.rights[j];
                        table += '<label><input checked="true" disabled="true" type="checkbox" alt="'+right.description+'" title="'+right.description+ '" />'
                        +((!right.right)?'&nbsp;':right.right)
                        +'</label>';
                    }
                    table += '</td>';

                    table += '</tr>';
                }

                table += '</tbody>';
                table += '</table>';

                return table;
            }
        });

        return table;

    },

    _onSubmit : function(context){
        var form = $("#create-profile-form").serialize();
        form+="&";
        form+=context.datatable.$('input').serialize();

        $.ajax({
            type : 'POST',
            url : context.options.url,
            data : form,
            success : function(data){
                $( "#"+STARTER.lastActiveModal()).find(".modal-content").html(data);
                $('body').trigger("modal-change-content");
            }
        });
    }

};