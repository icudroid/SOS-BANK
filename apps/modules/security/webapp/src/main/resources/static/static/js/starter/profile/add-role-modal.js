STARTER.jsPackage("starter.profile");

starter.profile.AddRoleModal = function(options,callback,context){
    this._init(options,callback,context);
};

starter.profile.AddRoleModal.prototype = {

    profile : null,
    allRoles : null,
    leftRole : null,
    datatable : null,

    _initLeftRole: function () {
        this.leftRole = [];
        for(var i in this.allRoles){
            var role = this.allRoles[i];

            var found = false;
            for (var j = 0; j < this.profile.roles.length; j++) {
                var r = this.profile.roles[j];
                if(r.id==role.id){
                    found = true;
                    break;
                }
            }

            if(!found){
                this.leftRole.push(role);
            }

        }
    },

    _createTableRole: function () {
        var table = "";
        for (var i = 0; i < this.leftRole.length; i++) {
            var role = this.leftRole[i];
            table += '<tr>';
                table += '<td> <input type="checkbox" data-id="'+role.id+'"/> </td>';
                table += '<td>'+role.name+'</td>';
                table += '<td>'+role.description+'</td>';
            table += '</tr>';
        }

        var $tbody = $("#table-roles").find("tbody");
        $tbody.html(table);
        this.datatable = $("#table-roles").DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/French.json"
            }
        });
    },

    _init : function(options,callback,context){
        var that = this;
        this.profile = options.profile;
        this.allRoles = options.roles;

        $("#add-role-submit").click(function(){

            var roleIdsToAdd = [];

            that.datatable.$("input[type='checkbox']:checked").each(function(){
                roleIdsToAdd.push($(this).data("id"));
            });

            STARTER.hideLastActiveModal();

            callback.call(context,roleIdsToAdd);
        });

        this._initLeftRole();

        $("#profile-name").html(this.profile.name);

        this._createTableRole();





    }
};