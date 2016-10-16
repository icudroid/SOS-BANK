STARTER.jsPackage("starter.utils.datatable");

/***
 Wrapper/Helper Class for datagrid based on jQuery Datatable Plugin
 ***/
starter.utils.datatable.ActionDatatable = function(options) {

    var tableOptions; // main options
    var dataTable; // datatable object
    var table; // actual table jquery object
    var tableContainer; // actual table container object
    var tableWrapper; // actual table wrapper jquery object
    var tableInitialized = false;
    var ajaxParams = {}; // set filter mode
    var nEditing = null; // row editing
    var the;


    var restoreRow = function (nRow) {
        dataTable.row( nRow ).draw();
    };

    var editRow = function (nRow) {
        var aData = dataTable.row( nRow ).data();

        var jqTds = $('>td', nRow);

        var notVisible = 0;

        for (var i = 0; i < tableOptions.dataTable.columns.length; i++) {
            var def = tableOptions.dataTable.columns[i];

            if(def.visible === false){
                notVisible++;
                continue;
            }


            var varName = def.data;

            switch (def.editableType){
                case 'text':
                    $(jqTds[i-notVisible]).html(tableOptions.htmlTemplate.editable.text.split("@@NAME@@").join(varName).split("@@VALUE@@").join(aData[def.data]));
                    break;
                case 'date' :
                    $(jqTds[i-notVisible]).html(tableOptions.htmlTemplate.editable.date.split("@@NAME@@").join(varName).split("@@VALUE@@").join(
                        STARTER.dateToString(aData[def.data])
                    ));
                    break;
                case 'select' :
                    var $select = $(tableOptions.htmlTemplate.editable.select.split("@@NAME@@").join(varName));
                    for (var j = 0; j < def.options.length; j++) {
                        var option = def.options[j];
                        if(option.value !==''){
                            var $option = $("<option value='" + option.value + "'>" + option.label + "</option>");
                            if(aData[def.data]=== true || aData[def.data] === false){
                                if(aData[def.data] ===  option.value){
                                    $option.attr("selected","selected");
                                }
                            }else{
                                if(aData[def.data].id ===  option.value){
                                    $option.attr("selected","selected");
                                }
                            }

                            $select.append($option);
                        }
                    }
                    $(jqTds[i-notVisible]).html($select);
                    break;
                case 'multiSelect' :
                    var $select = $(tableOptions.htmlTemplate.editable.multiSelect.split("@@NAME@@").join(varName));
                    for (var j = 0; j < def.options.length; j++) {
                        var option = def.options[j];
                        if(option.value !==''){
                            var $option = $("<option value='" + option.value + "'>" + option.label + "</option>");

                            for (var k = 0; k < aData[def.data].length; k++) {
                                if(aData[def.data][k].id ==  option.value){
                                    $option.attr("selected","selected");
                                }
                            }

                            $select.append($option);
                        }
                    }
                    $(jqTds[i-notVisible]).html($select);
                    break;
            }


            if(def.searchType=="btnSearch"){
                $(jqTds[i-notVisible]).html('<a class="btn default btn-xs green-stripe edit save" href="#">Enregister</a><a class="btn default btn-xs red-stripe cancel" href="#">Annuler</a>');
            }
        }

        $(".editable-select").select2();
        $(".editable-date-picker").datepicker({
            orientation: "left",
            autoclose: true,
            format: "dd/mm/yyyy",
            language: "fr"
        });
    };

    var saveRow = function( nRow) {

        var data = dataTable.row( nRow ).data();

        var $nRow = $(nRow);

        var dataSend = {};

        for (var i = 0; i < tableOptions.dataTable.columns.length; i++) {
            var def = tableOptions.dataTable.columns[i];

            var varName = def.data;


            switch (def.editableType) {
                case 'text':
                case 'select' :
                case 'multiSelect' :
                    dataSend[varName] = $nRow.find('[name="' + varName + '"]').val();
                    break;
                case 'date' :
                    dataSend[varName] = STARTER.parseDate($nRow.find('[name="' + varName + '"]').val());
                    break;
                case 'value' :
                    dataSend[varName] = data[def.data];
                    break;
            }
        }

        STARTER.postJSON(options.editable.saveUrl,dataSend,function(data, textStatus, jqXHR){
            if(data.resultCode!= 0){
                $nRow.find(".help-block-error").remove();
                for (var i = 0; i < data.data.length; i++) {
                    var err = data.data[i];
                    var inputName = err.name.split(".").join("\\.");
                    var $divFormCtrl = $("[name='"+inputName+"']",$nRow).parent();
                    $divFormCtrl.addClass("has-error");
                    if(typeof i18n == "undefined")
                        $divFormCtrl.append("<p class='help-block help-block-error'>"+err.error+"</p>");
                    else
                        $divFormCtrl.append("<p class='help-block help-block-error'>"+err.error+"</p>");
                }

            }else{
                nEditing = null;
                dataTable.ajax.reload(null,false);
            }
        });
    };

    var handleEditable = function(){
        table.on('click', '.cancel', function (e) {
            e.preventDefault();
            restoreRow(nEditing);
            nEditing = null;
        });
        table.on('click', '.edit', function (e) {
            e.preventDefault();

            /* Get the row as a parent of the link that was clicked on */
            var nRow = $(this).parents('tr')[0];

            if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                var indexTr = dataTable.row( nRow).index();
                dataTable.one("draw.dt",function(){
                    nRow = dataTable.row( indexTr).node();
                    editRow(nRow);
                    nEditing = nRow;
                });
                restoreRow(nEditing);
            } else if (nEditing == nRow && $(this).hasClass('save')) {
                /* Editing this row and want to save it */
                saveRow(nEditing);
            } else {
                /* No edit in progress - let's start one */
                editRow(nRow);
                nEditing = nRow;
            }
        });
    };

    var handleEnterInInputField= function (){
        table.find("input.form-filter").keydown(function(event){
            if(event.keyCode==13){
                table.find("button.filter-submit").click();
            }
        });
    };

    var handleAdvancedSearch = function(){
        if(tableOptions.hiddableSearch){
            table.find(".filter").toggle();
            table.find(".advanced-search-btn").click(function(e){
                e.preventDefault();
                var $elt = $(this);
                var $i = $elt.find("i");

                if($i.hasClass("fa-angle-up")){
                    $i.removeClass("fa-angle-up");
                    $i.addClass("fa-angle-down");;
                }else{
                    $i.removeClass("fa-angle-down")
                    $i.addClass("fa-angle-up");
                }
                table.find(".filter").toggle();
            });
        }


    };



    var countSelectedRecords = function() {
        var selected = $('tbody > tr > td:nth-child(1) input[type="checkbox"]:checked', table).size();
        var text = tableOptions.dataTable.language.metronicGroupActions;
        if (selected > 0) {
            $('.table-group-actions > span', tableWrapper).text(text.replace("_TOTAL_", selected));
        } else {
            $('.table-group-actions > span', tableWrapper).text("");
        }
    };


    var createActionnable = function() {
        if(tableOptions.actionnable.active){
            var selectOptions = "";
            for (var i = 0; i < tableOptions.actionnable.actions.length; i++) {
                var action = tableOptions.actionnable.actions[i];
                selectOptions+='<option value="'+action.value+'">'+action.label+'</option>';
            }
            $(tableOptions.htmlTemplate.actions.split("@@OPTIONS@@").join(selectOptions)).insertBefore(table);
        }
    };


    var createSearchableRow = function(){
        table.html('');
        var $thead = $("<thead>");
        var $heading = $("<tr role='row' class='heading'>");

        for (var i = 0; i < tableOptions.dataTable.columns.length; i++) {
            var def = tableOptions.dataTable.columns[i];
            if(typeof def.searchType == 'undefined') {
                def.searchType = 'none';
            }

            if(typeof def.checkboxable != 'undefined' && def.checkboxable){
                $heading.append($(tableOptions.htmlTemplate.checkboxHeadHtml.split("@@SOURCE_TABLE@@").join(table.attr("id"))));
            }else if(def.searchType == 'btnSearch'){
                if(tableOptions.hiddableSearch){
                    $heading.append($(tableOptions.htmlTemplate.searchHeadHiddableHtml));
                }else{
                    $heading.append($(tableOptions.htmlTemplate.searchHeadHtml));
                }
            }else{
                $heading.append($("<th>"));
            }
        }
        $thead.append($heading);

        var $filter = $("<tr role='row' class='filter'>");

        for (var i = 0; i < tableOptions.dataTable.columns.length; i++) {
            var def = tableOptions.dataTable.columns[i];

            var varName = '';

            if(typeof def.varName == 'undefined') {
                varName = def.data;
            }else{
                varName = def.varName;
            }


            switch (def.searchType){
                case 'text':
                    $filter.append($(tableOptions.htmlTemplate.textHtml.split("@@ID@@").join(varName)));
                    break;
                case 'period' :
                    $filter.append($(tableOptions.htmlTemplate.periodHtml.split("@@ID@@").join(varName)));
                    break;
                case 'date' :
                    $filter.append($(tableOptions.htmlTemplate.dateHtml.split("@@ID@@").join(varName)));
                    break;
                case 'interval' :
                    $filter.append($(tableOptions.htmlTemplate.intervalHtml.split("@@ID@@").join(varName)));
                    break;
                case 'customSelect':
                    var $Tdselect = $(tableOptions.htmlTemplate.selectHtml.split("@@ID@@").join(varName));
                    var $select =$Tdselect.find("select");

                    if(typeof def.optionRenderLabel == 'undefined') {
                        def.optionRenderLabel = function(text){
                            return text;
                        }
                    }

                    for (var j = 0; j < def.optionGroups.length; j++) {
                        var selectOptions = def.optionGroups[j];
                        if("option"==selectOptions.type){
                            $select.append($("<option data-cat='main' value='"+selectOptions.value+"'>"+def.optionRenderLabel(selectOptions.label)+"</option>"));
                        }else{
                            $select.append($("<option data-cat='main' value='"+selectOptions.value+"'>"+def.optionRenderLabel(selectOptions.label)+"</option>"));
                            if(typeof def.groupsOnly == 'undefined' || def.groupsOnly == false){
                                for (var k = 0; k < selectOptions.options.length; k++) {
                                    var option = selectOptions.options[k];
                                    $select.append($("<option data-cat='sub' value='"+option.value+"'>"+def.optionRenderLabel(option.label)+"</option>"));
                                }
                            }
                        }
                    }
                    $filter.append($Tdselect);
                    break;
                case 'select' :
                    var $Tdselect = $(tableOptions.htmlTemplate.selectHtml.split("@@ID@@").join(varName));
                    var $select =$Tdselect.find("select");
                    for (var j = 0; j < def.options.length; j++) {
                        var option = def.options[j];
                        $select.append($("<option value='"+option.value+"'>"+option.label+"</option>"));
                    }
                    $filter.append($Tdselect);
                    break;
                case 'multiselect' :
                    var $Tdselect = $(tableOptions.htmlTemplate.multiSelectHtml.split("@@ID@@").join(varName));
                    var $select =$Tdselect.find("select");
                    for (var j = 0; j < def.optionGroups.length; j++) {
                        var optionGroup = def.optionGroups[j];
                        if("option"==optionGroup.type){
                            $select.append($("<option value='"+optionGroup.value+"'>"+optionGroup.label+"</option>"));
                        }else{
                            var $optgroup = $("<optgroup label='"+optionGroup.label+"'>");
                            for (var k = 0; k < optionGroup.options.length; k++) {
                                var option = optionGroup.options[k];
                                $optgroup.append($("<option value='"+option.value+"'>"+option.label+"</option>"));
                            }
                            $select.append($optgroup);
                        }
                    }
                    $filter.append($Tdselect);
                    break;
                case 'btnSearch' :
                    $filter.append($(tableOptions.htmlTemplate.searchBtn));
                    break;
                    break;
                default :
                    $filter.append($("<td>"));
                    break;
            }

        }


        $thead.append($filter);

        table.append($thead);
        table.append($('<tbody>'));
    };



    var handlePickers = function () {
        if (!$().datepicker) {
            return;
        }
        //init date pickers
        $('.date-picker').datepicker({
            orientation: "left",
            autoclose: true,
            format: "dd/mm/yyyy",
            language: "fr"
        });

        if (!$().datetimepicker) {
            return;
        }

        $(".date-time-picker").datetimepicker({
            locale: "fr",
            sideBySide: true
        });
    };


    //main function to initiate the module
    var init =  function(options) {


        if (!$().dataTable) {
            return;
        }


        if(typeof options.htmlTemplate == 'undefined'){
            options.htmlTemplate = {};
        }

        var i18n = $.extend(true, {
            period : {
                from : "De",
                to : "à"
            },
            interval : {
                from : "De",
                to : "à"
            },
            searchHeadHiddable :{
                title : 'Actions',
                label : 'Recherche <i class="fa fa-angle-down"></i>'
            },
            searchHead : {
                title : 'Actions'
            },
            searchBtn : {
                submit : '<i class="fa fa-search"></i> Rechercher',
                reset : '<i class="fa fa-times"></i> RAZ'
            },
            actionsBtn : {
                submit : '<i class="fa fa-check"></i> Exécuter'
            }

        },options.htmlTemplate.i18n);

        // default settings
        options = $.extend(true, {

            htmlTemplate : {

                dateHtml :    '<td>'
                + '<div class="input-group date date-picker margin-bottom-5">'
                +   '<input class="form-control form-filter input-sm" readonly="readonly" id="@@ID@@" name="@@ID@@" type="text"/>'
                +   '<span class="input-group-btn">'
                +       '<button class="btn btn-sm default" type="button"><i class="fa fa-calendar"></i></button>'
                +   '</span>'
                + '</div>'
                + '</td>',


                periodHtml :'<td>'
                + '<div class="input-group date date-picker margin-bottom-5">'
                +   '<input class="form-control form-filter input-sm" readonly="readonly" placeholder="'+i18n.period.from+'" id="@@ID@@From" name="@@ID@@From"  type="text"/>'
                +   '<span class="input-group-btn">'
                +       '<button class="btn btn-sm default" type="button"><i class="fa fa-calendar"></i></button>'
                +   '</span>'
                + '</div>'
                +'<div class="input-group date date-picker">'
                +   '<input class="form-control form-filter input-sm" readonly="readonly" placeholder="'+i18n.period.to+'" id="@@ID@@To" name="@@ID@@To"  type="text"/>'
                +   '<span class="input-group-btn">'
                +       '<button class="btn btn-sm default" type="button"><i class="fa fa-calendar"></i></button>'
                +   '</span>'
                + '</div>'
                + '</td>',


                textHtml : "<td><input type='text' name='@@ID@@' id='@@ID@@' class='form-control form-filter input-sm'></td>",

                intervalHtml : '<td>'
                + '<div class="margin-bottom-5">'
                +   '<input class="form-control form-filter input-sm" placeholder="'+i18n.interval.from+'" id="@@ID@@From" name="@@ID@@From" value="" type="text">'
                + '</div>'
                + '<input class="form-control form-filter input-sm" placeholder="'+i18n.interval.to+'" id="@@ID@@To" name="@@ID@@To" value="" type="text">'
                + '</td>',

                selectHtml : '<td>'
                + '<select class="form-control form-filter input-sm" id="@@ID@@" name="@@ID@@">'
                + '</select>'
                + '</td>',

                multiSelectHtml : '<td>'
                + '<select multiple="true" class="form-control form-filter input-sm" id="@@ID@@" name="@@ID@@">'
                + '</select>'
                + '</td>',

                searchHeadHiddableHtml : '<th>'
                + '<span class="pull-left">'+i18n.searchHeadHiddable.title+'</span>'
                + '<span class="pull-right"><a href="javascript:;" class="advanced-search-btn">'+i18n.searchHeadHiddable.label+'</a></span>'
                + '</th>',

                searchHeadHtml : '<th>'+i18n.searchHead.title+'</th>',

                searchBtn  : '<td>'
                +   '<button class="btn default green-stripe filter-submit margin-bottom ">'+i18n.searchBtn.submit+'</button>'
                +   '<button class="btn default red-stripe filter-cancel">'+i18n.searchBtn.reset+'</button>'
                + '</td>',


                checkboxHeadHtml :  '<th class="table-checkbox">'
                + '<input type="checkbox" class="group-checkable" data-set="#@@SOURCE_TABLE@@ .checkboxes"/>'
                + '</th>',

                actions : '<div class="table-actions-wrapper">'
                + '<span>'
                + '</span>'
                + '<select class="table-group-action-input form-control input-inline input-sm">@@OPTIONS@@</select>'
                + '<button class="btn btn-sm yellow table-group-action-submit">'+i18n.actionsBtn.submit+'</button>'
                + '</div>',


                editable : {
                    text : '<input type="text" class="form-control" value="@@VALUE@@" name="@@NAME@@">',
                    date : '<div class="input-group date date-picker margin-bottom-5 editable-date-picker" data-date-format="dd/mm/yyyy">'+
                    '<input type="text" class="form-control form-filter input-sm" readonly="readonly" name="@@NAME@@" placeholder="JJ/MM/AAAA" value="@@VALUE@@">'+
                    '<span class="input-group-btn">'+
                    '<button class="btn btn-sm default" type="button"><i class="fa fa-calendar"></i></button>'+
                    '</span>'+
                    '</div>',
                    select : '<select class="editable-select" name="@@NAME@@"></select>',
                    multiSelect : '<select class="editable-select" name="@@NAME@@" multiple="multiple"></select>'
                }

            },


            actionnable :{
                noActionSelected : 'Vous devez choisir une action à appliquer aux enregistrements sélectionnés',
                noRowSelected : 'Aucuns enregistrements sélectionnés',
                active : false,
                actions :[],
                doAction : function(data){}
            },
            editable : {
                active : false,
                saveUrl : null
            },
            hiddableSearch : false,
            src: "", // actual table
            filterApplyAction: "filter",
            filterCancelAction: "filter_cancel",
            resetGroupActionInputOnSuccess: true,
            loadingMessage: 'Chargement ...',
            dataTable: {
                "dom": "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r><'table-scrollable't><'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>>", // datatable layout
                "pageLength": 10, // default records per page
                "language": { // language settings
                    // metronic spesific
                    "metronicGroupActions": "_TOTAL_ Enregistrements sélectionés:  ",
                    "metronicAjaxRequestGeneralError": "Could not complete request. Please check your internet connection",

                    // data tables spesific
                    "lengthMenu": "<span class='seperator'>|</span>Voir _MENU_ enregistrements",
                    "info": "<span class='seperator'>|</span>_TOTAL_ enregistrements totals trouvés",
                    "infoEmpty": "Pas d'enregistrement trouvé à voir",
                    "emptyTable": "Pas de données disponible dans la table",
                    "zeroRecords": "Pas d'enregistrement correspondant trouvé",
                    "paginate": {
                        "previous": "Précédent",
                        "next": "Suivant",
                        "last": ">|",
                        "first": "|<",
                        "page": "Page",
                        "pageOf": "de"
                    }
                },

                "orderCellsTop": true,

                "pagingType": "bootstrap_extended", // pagination type(bootstrap, bootstrap_full_number or bootstrap_extended)
                "autoWidth": false, // disable fixed width and enable fluid table
                "processing": false, // enable/disable display message box on record load
                "serverSide": true, // enable/disable server side ajax loading

                "ajax": { // define ajax settings
                    "url": "", // ajax URL
                    "type": "POST", // request type
                    "timeout": 20000,
                    "data": function(data) { // add request parameters before submit
                        $.each(ajaxParams, function(key, value) {
                            data[key] = value;
                        });
                        App.blockUI({
                            message: tableOptions.loadingMessage,
                            target: tableContainer,
                            overlayColor: 'none',
                            cenrerY: true,
                            boxed: true
                        });
                    },
                    "dataSrc": function(res) { // Manipulate the data returned from the server
                        if (res.customActionMessage) {
                            App.alert({
                                type: (res.customActionStatus == 'OK' ? 'success' : 'danger'),
                                icon: (res.customActionStatus == 'OK' ? 'check' : 'warning'),
                                message: res.customActionMessage,
                                container: tableWrapper,
                                place: 'prepend'
                            });
                        }

                        if (res.customActionStatus) {
                            if (tableOptions.resetGroupActionInputOnSuccess) {
                                $('.table-group-action-input', tableWrapper).val("");
                            }
                        }

                        if ($('.group-checkable', table).size() === 1) {
                            $('.group-checkable', table).attr("checked", false);
                            $.uniform.update($('.group-checkable', table));
                        }

                        if (tableOptions.onSuccess) {
                            tableOptions.onSuccess.call(undefined, the);
                        }

                        App.unblockUI(tableContainer);

                        return res.data;
                    },
                    "error": function() { // handle general connection errors
                        if (tableOptions.onError) {
                            tableOptions.onError.call(undefined, the);
                        }

                        App.alert({
                            type: 'danger',
                            icon: 'warning',
                            message: tableOptions.dataTable.language.metronicAjaxRequestGeneralError,
                            container: tableWrapper,
                            place: 'prepend'
                        });

                        App.unblockUI(tableContainer);
                    }
                },

                "drawCallback": function(oSettings) { // run some code on table redraw
                    if (tableInitialized === false) { // check if table has been initialized
                        tableInitialized = true; // set table initialized
                        table.show(); // display table
                    }
                    //App.initUniform($('input[type="checkbox"]', table)); // reinitialize uniform checkboxes on each table reload
                    countSelectedRecords(); // reset selected records indicator

                    // callback for ajax data load
                    if (tableOptions.onDataLoad) {
                        tableOptions.onDataLoad.call(undefined, the);
                    }
                },

                fnServerParams : function(data){
                    data.additionalForm = {};

                    if(!options.dataTable.additionalParams){
                        for (var param in options.additionalParams){
                            data.additionalForm[param] = options.additionalParams[param];
                        }
                    }

                    for (var i = 0; i < options.dataTable.columns.length; i++) {
                        var def = options.dataTable.columns[i];

                        var varName = '';

                        if(typeof def.varName == 'undefined') {
                            varName = def.data;
                        }else{
                            varName = def.varName;
                        }

                        switch (def.searchType){
                            case 'text':
                                data.additionalForm[varName] = $('#'+varName).val();
                                break;
                            case 'period' :
                                data.additionalForm[varName+'From'] = STARTER.parseDate($('#'+varName+'From').val());
                                data.additionalForm[varName+'To'] = STARTER.parseDate($('#'+varName+'To').val());
                                break;
                            case 'date' :
                                data.additionalForm[varName] = STARTER.parseDate($('#'+varName).val());
                                break;
                            case 'interval' :
                                data.additionalForm[varName+'From'] = $('#'+varName+'From').val();
                                data.additionalForm[varName+'To'] = $('#'+varName+'To').val();
                                break;
                            case 'select':
                            case 'customSelect':
                                data.additionalForm[varName] = $('#'+varName).val();
                                break;
                            case 'multiselect' :
                                data.additionalForm[varName] = $('#'+varName).val();
                                break;
                        }

                    }


                }
            }

        }, options);


        if(options.dataTable.columns[0].checkboxable){
            options.dataTable.columns[0].orderable = false;
        }

        tableOptions = options;

        // create table's jquery object
        table = $(options.src);
        tableContainer = table.parents(".table-container");


        createSearchableRow();
        createActionnable();

        // apply the special class that used to restyle the default datatable
        var tmp = $.fn.dataTableExt.oStdClasses;

        $.fn.dataTableExt.oStdClasses.sWrapper = $.fn.dataTableExt.oStdClasses.sWrapper + " dataTables_extended_wrapper";
        $.fn.dataTableExt.oStdClasses.sFilterInput = "form-control input-small input-sm input-inline";
        $.fn.dataTableExt.oStdClasses.sLengthSelect = "form-control input-xsmall input-sm input-inline";

        // initialize a datatable
        dataTable = table.DataTable(options.dataTable);

        // revert back to default
        $.fn.dataTableExt.oStdClasses.sWrapper = tmp.sWrapper;
        $.fn.dataTableExt.oStdClasses.sFilterInput = tmp.sFilterInput;
        $.fn.dataTableExt.oStdClasses.sLengthSelect = tmp.sLengthSelect;

        // get table wrapper
        tableWrapper = table.parents('.dataTables_wrapper');

        // build table group actions panel
        if ($('.table-actions-wrapper', tableContainer).size() === 1) {
            $('.table-group-actions', tableWrapper).html($('.table-actions-wrapper', tableContainer).html()); // place the panel inside the wrapper
            $('.table-actions-wrapper', tableContainer).remove(); // remove the template container
        }
        // handle group checkboxes check/uncheck
        $('.group-checkable', table).change(function() {
            var set = $('tbody > tr > td:nth-child(1) input[type="checkbox"]', table);
            var checked = $(this).is(":checked");
            $(set).each(function() {
                $(this).attr("checked", checked);
            });
            $.uniform.update(set);
            countSelectedRecords();
        });

        // handle row's checkbox click
        table.on('change', 'tbody > tr > td:nth-child(1) input[type="checkbox"]', function() {
            countSelectedRecords();
        });

        // handle filter submit button click
        table.on('click', '.filter-submit', function(e) {
            e.preventDefault();
            the.submitFilter();
        });

        // handle filter cancel button click
        table.on('click', '.filter-cancel', function(e) {
            e.preventDefault();
            the.resetFilter();
        });


        tableWrapper.on('click', '.table-group-action-submit', function (e) {
            e.preventDefault();
            var action = $(".table-group-action-input", tableWrapper);
            if (action.val() != "" && the.getSelectedRowsCount() > 0) {
                var data = {
                    action : action.val(),
                    ids : the.getSelectedRows()
                };
                options.actionnable.doAction(data);

            } else if (action.val() == "") {
                App.alert({
                    type: 'danger',
                    icon: 'warning',
                    message: options.actionnable.noActionSelected,
                    container: tableWrapper,
                    place: 'prepend'
                });
            } else if (the.getSelectedRowsCount() === 0) {
                App.alert({
                    type: 'danger',
                    icon: 'warning',
                    message: options.actionnable.noRowSelected,
                    container: tableWrapper,
                    place: 'prepend'
                });
            }
        });



        if(options.editable.active){
            handleEditable();
        }

    };


    the =  {


        submitFilter: function() {
            the.setAjaxParam("action", tableOptions.filterApplyAction);

            // get all typeable inputs
            $('textarea.form-filter, select.form-filter, input.form-filter:not([type="radio"],[type="checkbox"])', table).each(function() {
                the.setAjaxParam($(this).attr("name"), $(this).val());
            });

            // get all checkboxes
            $('input.form-filter[type="checkbox"]:checked', table).each(function() {
                the.addAjaxParam($(this).attr("name"), $(this).val());
            });

            // get all radio buttons
            $('input.form-filter[type="radio"]:checked', table).each(function() {
                the.setAjaxParam($(this).attr("name"), $(this).val());
            });

            dataTable.ajax.reload();
        },

        resetFilter: function() {
            $('textarea.form-filter, select.form-filter, input.form-filter', table).each(function() {
                var $input = $(this);
                if($input.attr('type')!='hidden'){
                    $(this).val("");
                }
            });
            $('input.form-filter[type="checkbox"]', table).each(function() {
                $(this).attr("checked", false);
            });
            the.clearAjaxParams();
            the.addAjaxParam("action", tableOptions.filterCancelAction);
            dataTable.ajax.reload();
        },

        getSelectedRowsCount: function() {
            return $('tbody > tr > td:nth-child(1) input[type="checkbox"]:checked', table).size();
        },

        getSelectedRows: function() {
            var rows = [];
            $('tbody > tr > td:nth-child(1) input[type="checkbox"]:checked', table).each(function() {
                rows.push($(this).val());
            });

            return rows;
        },

        setAjaxParam: function(name, value) {
            ajaxParams[name] = value;
        },

        addAjaxParam: function(name, value) {
            if (!ajaxParams[name]) {
                ajaxParams[name] = [];
            }

            skip = false;
            for (var i = 0; i < (ajaxParams[name]).length; i++) { // check for duplicates
                if (ajaxParams[name][i] === value) {
                    skip = true;
                }
            }

            if (skip === false) {
                ajaxParams[name].push(value);
            }
        },

        clearAjaxParams: function(name, value) {
            ajaxParams = {};
        },

        getDataTable: function() {
            return dataTable;
        },

        getTableWrapper: function() {
            return tableWrapper;
        },

        gettableContainer: function() {
            return tableContainer;
        },

        getTable: function() {
            return table;
        }

    };


    init(options);


    handleAdvancedSearch();
    handleEnterInInputField();
    handlePickers();

    return the;

};