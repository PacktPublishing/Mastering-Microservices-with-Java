angular.module('otrsApp').factory('AjaxHandler', ['$injector', function ($injector) {

        var log = $injector.get('$log');
        var location = $injector.get('$location');
        var http = $injector.get('$http');
        var showSpinner = false;

        var ajaxHandler = {};

        var restBaseUrl;
        if (location.$$absUrl.indexOf('file://') === 0) {
            restBaseUrl = 'https://localhost:8765/api/v1';
            log.debug('local REST services');
        } else {
            restBaseUrl = 'https://localhost:8765/api/v1';
            log.debug('remote REST services');
        }

        ajaxHandler.getRestBaseUrl = function () {
            return restBaseUrl;
        };

        ajaxHandler.activateApi = function () {
            return http.get(restBaseUrl + '/utility/activate', null);
        };


        ajaxHandler.get = function (api) {
            return http.get(restBaseUrl + api, null)
        };

        ajaxHandler.startSpinner = function () {
            showSpinner = true;
        };

        ajaxHandler.stopSpinner = function () {
            showSpinner = false;
        };

        ajaxHandler.getSpinnerStatus = function () {
            return showSpinner;
        };

        return ajaxHandler;
    }]);