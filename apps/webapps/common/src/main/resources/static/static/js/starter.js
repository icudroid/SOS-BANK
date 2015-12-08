var STARTER = function() {
    var lastModalsId = [];

    var pad = function (number,nbDigit) {
        if(!nbDigit){
            nbDigit = 10;
        }else{
            nbDigit = 10 * nbDigit;
        }

        if (number < nbDigit) {
            return '0' + number;
        }
        return number;
    };

    var _i18nTranslate = function(i18nConfig,key){
        var translate = i18nConfig[key];
        return (translate)?translate:key;
    };


    return {
        jsPackage: function(name) {
            var p = name.split(".");
            var currentPackage = window;
            for (var i = 0; i < p.length; i++) {
                var packageName = p[i];
                if(!currentPackage[packageName]){
                    currentPackage[packageName] = {};
                }
                currentPackage = currentPackage[packageName];
            }
        },


        postJSON : function(url, data,success){
            $.ajax({
                url : url,
                type: 'POST',
                contentType : 'application/json',
                data : JSON.stringify(data),
                dataType: "json",
                success  : function(xhr, ajaxOptions, thrownError) {
                    Metronic.unblockUI();
                    success(xhr, ajaxOptions, thrownError)
                },
                error: function(xhr, ajaxOptions, thrownError) {
                    if(xhr.responseJSON){
                        bootbox.dialog({
                            message: "exception : "+xhr.responseJSON.exception+"<br/>message : "+xhr.responseJSON.message,
                            title:xhr.responseJSON.error +"("+xhr.responseJSON.status+")"
                        });
                    }else{
                        bootbox.dialog({
                            message: xhr.responseText
                        });
                    }
                }
            });
        },


        displayFieldError : function(errors,i18n){

            for (var i = 0; i < errors.length; i++) {
                var err = errors[i];
                var inputName = err.name.split(".").join("\\.");
                var $divFormCtrl = $("[name='"+inputName+"']").parent();
                $divFormCtrl.addClass("has-error");
                $divFormCtrl.append("<p class='help-block help-block-error'>"+i18n[err.errorKey]+"</p>");
            }

        },



        parseDate : function(dateString){
            var frDateReg = /(\d{2})\/(\d{2})\/(\d{4})/;
            var dateArray = frDateReg.exec(dateString);
            if(dateArray==null) return null;
            return new Date(dateArray[3],dateArray[2]-1,dateArray[1]);
        },



        datetimeToString : function(dateValue,returnIfNull){
            if(dateValue == null){
                if(!returnIfNull){
                    returnIfNull =  "";
                }
                return returnIfNull;
            }
            var date = new Date(dateValue);
            return pad(date.getDate())+"/"+pad(date.getMonth()+1)+"/"+date.getFullYear()+ " "+pad(date.getHours())+":"+pad(date.getMinutes())+":"+pad(date.getSeconds());
        },

        dateToString : function(dateValue,returnIfNull){
            if(dateValue == null){
                if(!returnIfNull){
                    returnIfNull =  "";
                }
                return returnIfNull;
            }
            var date = new Date(dateValue);
            return pad(date.getDate())+"/"+pad(date.getMonth()+1)+"/"+date.getFullYear();
        },


        j8LocalDateToString : function(j8Date){
            var date = new Date(j8Date.year,j8Date.monthValue,j8Date.dayOfMonth);
            return pad(date.getDate())+"/"+pad(date.getMonth()+1)+"/"+date.getFullYear();
        },

        amountFormat : function(amount,devise,placement){
            if(amount === null){
                return "N/A";
            }

            if(!devise){
                devise = "&euro;";
            }

            if(!placement){
                placement= 'after';
            }

            switch (placement){
                case 'before' :
                    return devise+"&nbsp;"+amount.toFixed(2);
                case 'after' :
                    return amount.toFixed(2)+"&nbsp;"+devise;
                default :
                    return amount.toFixed(2)+"&nbsp;"+devise;
            }


        },

        i18nTranslate : function(i18nConfig,key){
            return _i18nTranslate(i18nConfig,key)
        },

        binaryAjaxDl : function(url,data){
            Metronic.blockUI({animate: true});
            $.ajax({
                dataType: 'binary',
                processData: false,
                url : url,
                type: 'POST',
                contentType : 'application/json',
                data : JSON.stringify(data),
                success: function(result,status,xhr) {

                    var filename = "";
                    var disposition = xhr.getResponseHeader('Content-Disposition');
                    if (disposition && disposition.indexOf('filename') !== -1) {
                        var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                        var matches = filenameRegex.exec(disposition);
                        if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
                    }

                    Metronic.unblockUI();

                    var url = URL.createObjectURL(result);
                    var $a = $('<a />', {
                        'href': url,
                        'download': filename,
                        'text': "click"
                    }).hide().appendTo("body")[0].click();

                    setTimeout(function () { URL.revokeObjectURL(url); }, 100); // cleanup
                }
            });

        },

        lastActiveModal : function(){
            return lastModalsId.slice(-1)[0]
        },

        hideLastActiveModal : function(){
            var modalId = lastModalsId.pop();
            $('#'+modalId).modal('hide');
        },

        /**
         *
         * @param e
         * @param elt
         * @param typeModal : none, 'modal-lg', 'modal-sm', 'modal-full',
         */
        ajaxModal : function(e,elt,typeModal,callBack){
            e.preventDefault();

            $(".modal:not(:visible)").remove();


            if(!typeModal){
                typeModal = "";
            }

            var id = "modal-"+new Date().getTime();

            var html='<div class="modal modal-scrollable fade" id="'+id+'" role="basic" aria-hidden="true">';
            html+='<div class="modal-dialog '+typeModal+'">';
            html+='<div class="modal-content"> </div>';
            html+='</div>';
            html+='</div>';

            $("body").append(html);

            var $modal = $( "#"+id) ;

            $modal.find(".modal-content").load( elt.href, function(responseText, status, jqXHR) {
                if (status == "error") {
                    $modal.find(".modal-content").html(responseText);
                    $modal.modal();
                }else{
                    lastModalsId.push(id);
                    $modal.modal({backdrop: 'static'});

                    if(typeof callBack != "undefined"){
                        callBack();
                    }
                }
            });

        }
    }
}();
