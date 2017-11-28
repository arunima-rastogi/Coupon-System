var app = angular.module("adminModule", []);
app.controller('adminCtrl', function ($scope, $http) {
    $scope.client = 'admin';
    $scope.mode = 'comps';
    $scope.workingItem = {};
    $scope.selectedId = 1; { }
    $scope.itemCreateMode = false;
    $scope.items = getItems();
    workingItemElement = document.querySelector("#workingItem");

    $scope.changeScope = function () {
        if($(workingItemElement).hasClass('open')){
            $scope.closeWorkingItem();
        }
        getItems();
    }

    $scope.getItemById = function () {

        url = "admin/";
        if ($scope.mode.includes('comps')) {
            url += "company/" + $scope.selectedId;

        } else {
            url += "customer/" + $scope.selectedId;

        }

        $http({
            'method': "GET",
            'url': url,
        }).then(function mySuccess(response) {
            $scope.workingItem = response.data;
        }, function myError(response) {
            $scope.workingItem = response.statusText;
        });
    }

    $scope.openCreateNewItem = function () {
        $scope.workingItem = {};
        $scope.workingItem.id = 1;
        $scope.itemCreateMode = true;
        openWorkingItem();
    }

    $scope.closeWorkingItem = function () {
        $(workingItemElement).removeClass('open')
        $(workingItemElement).addClass('close')
    }

    $scope.openUpdateItem = function (item) {
        $scope.workingItem = item;
        $scope.itemCreateMode = false;
        openWorkingItem();
    }

    $scope.openDeleteConfirmation = function (item) {
        r = confirm("Delete item id:" + item.id + "?");
        if (r == true) {
            deleteItem(item);
            $scope.getItems();
        }
    }

    $scope.sendNewItem = function () {
        postItem();
        $scope.closeWorkingItem();
    }

    $scope.sendUpdateItem = function () {
        putItem();
        $scope.closeWorkingItem();
    }

    $scope.getItems = function () {
        getItems();
    }

    function getItems() {
        url = "admin/";
        if ($scope.mode.includes('comps')) {
            url += "company/all";

        } else {
            url += "customer/all";

        }

        $http({
            'method': "GET",
            'url': url,
        }).then(function mySuccess(response) {
            if (response.data instanceof Object && response.data.constructor === Object) {
                $scope.items = [response.data];

            } else {
                $scope.items = response.data;

            }

        }, function myError(response) {
            $scope.items = response.statusText;
        });
    }

    function openWorkingItem() {
        $(workingItemElement).removeClass('close')
        $(workingItemElement).addClass('open')
    }

    function postItem() {

        url = "admin/";
        if ($scope.mode.includes('comps')) {
            url += "company/";

        } else {
            url += "customer/";

        }

        $http.post(url, $scope.workingItem);
    }

    function deleteItem(item) {

        url = "admin/";
        if ($scope.mode.includes('comps')) {
            url += "company/";

        } else {
            url += "customer/";

        }

        $http({
            'url': url,
            dataType: "json",
            method: "DELETE",
            data: item,
            headers: {
                "Content-Type": "application/json"
            }
        });
    }


    function putItem() {

        url = "admin/";
        if ($scope.mode.includes('comps')) {
            url += "company/";

        } else {
            url += "customer/";

        }

        $http.put(url, $scope.workingItem);
        $scope.closeCreateNewMode();
    }



});// end of angular module