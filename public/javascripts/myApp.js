function Task(id, name, birthday, gift, giftStatus) {
    var self        = this;
    self.id         = id;
    self.name       = ko.observable(name);
    self.birthday   = ko.observable(birthday);
    self.gift       = ko.observable(gift);
    self.giftStatus = ko.observable(giftStatus);
}

ViewModelTasks = function() {
    var self = this;

    self.tasks = ko.observableArray([]);

    self.id         = "";
    self.name       = ko.observable("");
    self.birthday   = ko.observable("");
    self.gift       = ko.observable("");
    self.giftStatus = ko.observable("Куплено");

    self.availableGifts = ko.observableArray(['Куплено', 'Еще нет']);

    self.BETA = function(){
        console.log()
        console.log(self.id)
        console.log(self.name())
        console.log(self.birthday())
        console.log(self.gift())
        console.log(self.giftStatus())

    }

    self.getTasks = function(){
        jsRoutes.controllers.Application.tasks2().ajax({
            dataType    : "json",
            contentType : "charset=utf-8",
            success : function(result) {
                self.tasks.removeAll();
                var o = result.objects;
                for (i=0; i< o.length; i++){
                    console.log(o[i]);
                    self.tasks.push(new Task(o[i].id, o[i].name, o[i].birthday, o[i].gift, o[i].giftStatus));
                }
            },
            error : function(result) {
                console.log("Error: " + result);
            }
        });
    };

    self.updTaskPrep = function(task){
        console.log(task.id);
        var dateBirthday = new Date(task.birthday());
        console.log(dateBirthday);

        var dateString = "";

        dateString += dateBirthday.getFullYear() + "-";
        dateString += ('0' + (dateBirthday.getMonth()) + 1).slice(-2) + "-";
        dateString += ('0' + dateBirthday.getDate()).slice(-2);

        console.log(dateString);

        self.id = task.id
        self.name(task.name())
        self.birthday(dateString)
        self.gift(task.gift())
        self.giftStatus(task.giftStatus())

    }


    self.createTask = function(){
        console.log("CREATE")
         var task = new Task("", self.name(), self.birthday(), self.gift(), self.giftStatus())

         var dataJSON = ko.toJSON(task);
         console.log(dataJSON);

        jsRoutes.controllers.Application.createTask2().ajax({
            dataType    : 'json',
            contentType : 'application/json; charset=utf-8',
            data        : dataJSON,
            success : function(result){
                console.log("OKE");
                console.log(result);
                var o = result.newTask;
                self.tasks.push(new Task(o.id, o.name, o.birthday, o.gift, o.giftStatus));
            },
            error : function(result){
                console.log("Error: " + result);
            }
        });
    }

    self.updTask = function(){
        console.log("UPD");
    }

    self.createOrUpdate = function(){
        if( self.id == "" ) { self.createTask() } else { self.updTask() }
    }

    self.delTask = function(task){
    console.log(task.id);
        jsRoutes.controllers.Application.delTask2(task.id).ajax({
            contentType : "charset=utf-8",
            success : function(result) {
                self.tasks.remove(task);
            },
            error : function(result) {
                console.log("Error: " + result);
            }
        });
    }

    self.getTasks();
}

$( document ).ready(function() {
  ko.applyBindings(new ViewModelTasks());
});