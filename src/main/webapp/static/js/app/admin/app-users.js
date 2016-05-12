(function(){
	 var adminApp = angular.module('admin'); 
	  
	//User
	 adminApp.controller('AdminUsersController', ['$http', '$scope', 'Upload', '$uibModal', function($http, $scope, Upload, $uibModal){
		 var adminUsers = this;
		 adminUsers.accounts  = [];
		 adminUsers.userQ = "";
		 var messageDiv = angular.element( document.querySelector( '#messageDiv' ) );
		 adminUsers.pageSize = 10;
		 adminUsers.user = {};
		 adminUsers.isNew = false;
		 adminUsers.isAddEditState  =false;
		 adminUsers.message = "";
		 $http.get('../../json/userType.json').success(function(response){
			 adminUsers.userType = response;
				 
			});
		 
		 $http.get('/adminAuth/authenticated').success(function(response){
			 if(response.result == "false"){
				 window.location = "/admin/login";
			 } else {
				 adminUsers.list();
			 }
		 }); 
		 
		 adminUsers.deleteUser = function(theAccount){
			 console.log(theAccount.id);
			 if(confirm('Are you sure you would like to delete this User?')){
				 $http({
					 method: "POST",
					 url: '/admin/users/delete/',  
					 data:   theAccount, 
				 }).success(function(response){
					  adminUsers.list();
						 
					}).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					});
			 }
		 }
		 
		 adminUsers.edit = function(theUser){
			 theUser.passwd = "";
			 adminUsers.user = theUser;
			 adminUsers.isAddEditState = true;
			 adminUsers.isNew = false;

		 }
		 
		 adminUsers.cancel = function(){
			 adminUsers.isAddEditState = false; 
		 }
		 
		 adminUsers.create = function(){
			 adminUsers.user = {};
			 adminUsers.isAddEditState = true; 
			 adminUsers.isNew = true;
		 }

		 adminUsers.list = function(){
			 adminUsers.isAddEditState = false; 
			 $http.get('/admin/users/list').success(function(response){ 
				 if(!response || response.length == 0){
					 adminUsers.accounts = []; 
				 }else {
					 adminUsers.accounts = response; 
				 }  
			 });
		 }

		 adminUsers.save = function(theUser){
			 if(!$scope.signupForm.$valid){
				 messageDiv.removeClass("alert-success");
				 messageDiv.addClass("alert-danger");
				 $scope.message = "Please enter all required fields";
			 }else {
				 $http({
					 method: "POST",
					 url: '/admin/users/save',  
					 data:  adminUsers.user,
					 
				 }).success(function(response){
					  adminUsers.list();
						 
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
	 
	  
})();
 
 