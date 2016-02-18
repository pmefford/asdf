'use strict';

angular.module('patientfocusApp')
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password', {}, {});
    });

angular.module('patientfocusApp')
    .factory('PasswordResetInit', function ($resource) {
        return $resource('api/account/reset_password/init', {}, {})
    });

angular.module('patientfocusApp')
    .factory('PasswordResetFinish', function ($resource) {
        return $resource('api/account/reset_password/finish', {}, {})
    });
