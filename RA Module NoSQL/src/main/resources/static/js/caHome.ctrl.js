var app = angular.module("myApp",[]);

app.controller("Ctrl",Ctrl);

function Ctrl(DataServ){

this.xyz="from controller :-)";

this.showData = false;
var self = this;

this.selected = function(raOfficeCode){
    //alert("hello!!!"+this.dropDown);
//var raOfficeCode = this.dropDown;
DataServ.loadData(raOfficeCode)
.then(function(data){
    self.showData = true;
    //alert(data);
    self.loadedData = data;
})

}

this.changedValue = function(data){
alert("hello!!!zzzzzzzzzzzzz"+data);
}

}


app.service("DataServ",function($http){

	var self = this;

	self.loadData = function(raOfficeCode){

		var promise1 = $http.get('/rest/loadDataOfRaOfficeByCode/'+raOfficeCode);
		var promise2 = promise1.then(function(response){
			return response.data;
		});
		return promise2;

	}


});