var app = angular.module("myApp",[]);

app.controller("Ctrl",Ctrl);

function Ctrl(){

this.xyz="from controller :-)";
this.nothingChecked = true;
this.showVerifyBtn = false;
this.dataToVerify = [];
this.verifyAll = function(){
//this.addDataToBeVerified();
    if(this.isVerifyAll){
this.nothingChecked = false;
this.dataToVerify = this.requests;
    for(i = 0; i<= this.requests.length; i++){
            this.requests[i].verify = true;
           // this.dataToVerify = this.requests;
        }
    }else{
    this.nothingChecked = true;
    this.dataToVerify = [];
    for(i = 0; i<= this.requests.length; i++){
                this.requests[i].verify = false;
       }


    }
}


this.verify = function(){
   this.nothingChecked = true;
       this.dataToVerify = [];

       for(i = 0; i<= this.requests.length; i++){
               if(this.requests[i].verify == true){
                   this.nothingChecked = false;
                   break;
               }
           }

    for(i = 0; i<= this.requests.length; i++){
        if(this.requests[i].verify == true){
            this.dataToVerify.push(this.requests[i]);
        }
    }



}

this.addDataToBeVerified = function(){
alert("addDataToBeVerified");
    this.dataToVerify = 0;
    for(i = 0; i<= this.requests.length; i++){
            if(this.requests[i].verify == true){
                this.dataToVerify.push(this.requests[i]);
            }
}

}

}

