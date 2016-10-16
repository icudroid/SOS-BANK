var Login = function() {

    var settings = {
        i18n :{
            "username.required" : "Username is required.",
            "password.required" : "Password is required."
        },
        backstretch : {
            images : [
                "../assets/pages/img/login/bg1.jpg",
                "../assets/pages/img/login/bg2.jpg",
                "../assets/pages/img/login/bg3.jpg"
            ],
            config : {
                fade: 1000,
                duration: 8000
            }
        }

    };

    var handleLogin = function() {

        $('.login-form').validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                },
                "remember-me": {
                    required: false
                }
            },

            messages: {
                username: {
                    required: settings.i18n["username.required"]
                },
                password: {
                    required: settings.i18n["password.required"]
                }
            },

            invalidHandler: function(event, validator) { //display error alert on form submit
                $('.alert-danger', $('.login-form')).show();
            },

            highlight: function(element) { // hightlight error inputs
                $(element)
                    .closest('.form-group').addClass('has-error'); // set error class to the control group
            },

            success: function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement: function(error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },

            submitHandler: function(form) {
                form.submit(); // form validation success, call ajax form submit
            }
        });

        $('.login-form input').keypress(function(e) {
            if (e.which == 13) {
                var $login = $('.login-form');
                if ($login.validate().form()) {
                    $login.submit(); //form validation success, call ajax form submit
                }
                return false;
            }
        });

        $('.forget-form input').keypress(function(e) {
            if (e.which == 13) {
                var $forget = $('.forget-form');
                if ($forget.validate().form()) {
                    $forget.submit();
                }
                return false;
            }
        });

        $('#forget-password').click(function(){
            $('.login-form').hide();
            $('.forget-form').show();
        });

        $('#back-btn').click(function(){
            $('.login-form').show();
            $('.forget-form').hide();
        });
    };


    var initSettings = function(settings) {
        this.settings = $.extend(true, this.settings ,settings);
    };

    var handleBg = function () {
        // init background slide images
        $('.login-bg').backstretch(
            settings.backstretch.images,
            settings.backstretch.config
        );
    };


    var handleForgetPwd = function () {
        $('.forget-form').hide();
    };

    return {
        //main function to initiate the module
        init: function(settings) {
            this.initSettings(settings);

            handleLogin();
            handleBg();
            handleForgetPwd();

        }

    };

}();