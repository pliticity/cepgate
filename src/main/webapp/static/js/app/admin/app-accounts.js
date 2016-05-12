(function(){
	 var adminApp = angular.module('admin');   
	 
	 //Accounts Controller 
	 adminApp.controller('AdminAccountsController', ['$http', '$scope', 'Upload', '$uibModal', function($http, $scope, Upload, $uibModal){
		 var adminAccounts = this;
		 adminAccounts.accounts  = [];
		 $scope.accountQ = "";
		 var messageDiv = angular.element( document.querySelector( '#messageDiv' ) );
		 $scope.accountPageSize = 10;
		 adminAccounts.account = {};
		 adminAccounts.currentAccount = {};
		 adminAccounts.isNew = false; 
		 adminAccounts.isNewUser = false;  
		 adminAccounts.message = "";
		 adminAccounts.currentState = "accounts";
		 $http.get('../../json/countries.json').success(function(response){
				$scope.countries = response;
				 
			});
		 $http.get('../../json/userType.json').success(function(response){
			 adminAccounts.userType = response;
				 
			});
		 
		 
		 $http.get('/adminAuth/authenticated').success(function(response){
			 if(response.result == "false"){
				 window.location = "/admin/login";
			 } else {
				 adminAccounts.list();
			 }
		 }); 
		 
		 
		 adminAccounts.toggleAccount = function(theAccount, active){
			 console.log(theAccount.id);
			 theAccount.active = active;
			 var action = active == "Yes"? "Enable": "Disable";
			 if(confirm('Are you sure you would like to ' + action + ' this Account?')){
				 $http({
					 method: "POST",
					 url: '/admin/accounts/toggle/',  
					 data:   theAccount, 
				 }).success(function(response){
					  adminAccounts.list();
						 
					}).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					});
			 }
		 }
		 
		 adminAccounts.deleteAccount = function(theAccount){
			 console.log(theAccount.id);
			 if(confirm('Are you sure you would like to delete this Account?')){
				 $http({
					 method: "POST",
					 url: '/admin/accounts/delete/',  
					 data:   theAccount, 
				 }).success(function(response){
					  adminAccounts.list();
						 
					}).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					});
			 }
		 }
		 
		 adminAccounts.isState = function(testState){
			 return adminAccounts.currentState == testState;
		 }
		 
		 adminAccounts.edit = function(theAccount){
			 theAccount.passwd = "";
			 adminAccounts.account = theAccount;
			 adminAccounts.currentState = "account_edit"; 
			 adminAccounts.isNew = false;
		 }
		 
		 adminAccounts.cancel = function(){
			 adminAccounts.currentState = "accounts";
		 }
		 
		 adminAccounts.create = function(){
			 adminAccounts.account = {};
			 adminAccounts.currentState = "account_edit"; 
			 adminAccounts.isNew = true;
		 }

		 adminAccounts.list = function(){
			 adminAccounts.currentState = "accounts";
			 $http.get('/admin/accounts/list').success(function(response){ 
				 if(!response || response.length == 0){
					 adminAccounts.accounts = []; 
				 }else {
					 adminAccounts.accounts = response; 
				 }  
			 });
		 }

		 adminAccounts.save = function(theAccount){
			 alert("save");
			 if(!$scope.signupForm.$valid){
				 messageDiv.removeClass("alert-success");
				 messageDiv.addClass("alert-danger");
				 $scope.message = "Please enter all required fields";
			 }else {
				 $http({
					 method: "POST",
					 url: '/admin/accounts/save',  
					 data:  adminAccounts.account,
					 
				 }).success(function(response){
					  adminAccounts.list();
						 
					}).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					}); 
			 } 
		 }
		 
		 adminAccounts.deleteUser = function(theAccount){
			 console.log(theAccount.id);
			 if(confirm('Are you sure you would like to delete this User?')){
				 $http({
					 method: "POST",
					 url: '/admin/clientusers/delete/',  
					 data:   theAccount, 
				 }).success(function(response){
					  adminAccounts.list();
						 
					}).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					});
			 }
		 }
		 
		 adminAccounts.editUser = function(theUser){
			 theUser.passwd = "";
			 adminAccounts.user = theUser;
			 adminAccounts.currentState = "user_edit";
			 adminAccounts.isNew = false; 
		 }
		 
		 adminAccounts.cancelUsers = function(){
			 adminAccounts.currentState = "user_edit";
			 adminAccounts.listUsers(adminAccounts.currentAccount);
		 }
		 
		 adminAccounts.createUser = function(){
			 adminAccounts.user = {}; 
			 adminAccounts.currentState = "user_edit";
			 adminAccounts.isNew = true;
		 }

		 adminAccounts.listUsers = function(anAccount){
			 adminAccounts.currentState = "users"; 
			 adminAccounts.currentAccount = anAccount;
			 $http({
				 method: "POST",
				 url: '/admin/clientusers/list',
				data: anAccount
			 }).success(function(response){ 
				 if(!response || !response.data || response.data.length == 0 ){
					 adminAccounts.accounts = []; 
					 if(response.result == "fail"){
						 messageDiv.removeClass("alert-success");
						 messageDiv.addClass("alert-danger");
						 adminAccounts.message = response.message;
					 }
				 }else {
					 adminAccounts.users = response.data; 
					 adminAccounts.message = "";
				 }  
			 });
		 }

		 adminAccounts.saveUser = function(theUser){
			 if(!$scope.signupForm.$valid){
				 messageDiv.removeClass("alert-success");
				 messageDiv.addClass("alert-danger");
				 $scope.message = "Please enter all required fields";
			 }else {
				 $http({
					 method: "POST",
					 url: '/admin/clientusers/save',  
					 data:  adminAccounts.user,
					 
				 }).success(function(response){
					  
					  if(response.result == "fail"){
						 messageDiv.removeClass("alert-success");
						 messageDiv.addClass("alert-danger");
						 adminAccounts.message = response.message;
					 }else {
						 adminAccounts.listUsers(adminAccounts.currentAccount);
					 }
						 
				 }).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					});
				 
			 }
		 }
			 
		 
	 }]);

	 
	 adminApp.config(['$compileProvider',
         function ($compileProvider) {
             $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
     }]);

 
})();
 
 