var app = angular.module("myApp",[]);

app.controller("Ctrl",Ctrl);

function Ctrl(ReqServ){

this.xyz="from ra view sub Cert reqs :-)";
this.page = 0;
var self = this;
this.loadMoreClicked = function(){
++self.page;
//alert("load More Clicked Page:"+self.page);
ReqServ.getReqs(self.page)
.then(function(data){
     if(data.length < 20){
     self.disableLoadMore = true;
     }
    //alert(data.length)
    self.requests = self.requests.concat(data);
})
}

}


app.service("ReqServ",function($http){

	var self = this;

	self.getReqs = function(page){

		var promise1 = $http.get('/rest/getSubCertRequestsForCa/'+page);
		var promise2 = promise1.then(function(response){
			return response.data;
		});
		return promise2;

	}


});