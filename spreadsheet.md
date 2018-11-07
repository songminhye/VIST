nfc에 카드가 태깅되면 spreadsheet에 저장되게한다. 
spreadsheet에서 정보를 가져와서 firebase에 저장되게한다. 

1. User 
[spreadsheet]
<https://docs.google.com/spreadsheets/d/1Ky18cNLftrZCcH0kuFij8dk05H6UuZFmiMSV3MeVAa0/edit?usp=sharing>

[script]
function writeDataToFirebase() {
  var sheet = SpreadsheetApp.openById("1Ky18cNLftrZCcH0kuFij8dk05H6UuZFmiMSV3MeVAa0");
  var mysheet = sheet.setActiveSheet(sheet.getSheets()[0]);
  var data = mysheet.getDataRange().getValues();
  var dataToImport = {};
  
  
  var firebaseUrl = "https://vist-rfid.firebaseio.com/";
  var secret = "jDdGbaqymeSbpEgXAty5OHnOI47TDSEqhljxpzRg";
  var base = FirebaseApp.getDatabaseByUrl(firebaseUrl,secret);
  
 
  for(var i = 1; i < data.length; i++) {
    var ID = data[i][1];
    
    dataToImport[ID] = "로그인 필요";
  }
 
  base.setData("User", dataToImport);
}


2.RFID
[spreadsheet]
<https://docs.google.com/spreadsheets/d/1_BvT5diDqs2XiD1tvIDaTf_a5EBuOgvmQ2MgfDprU8I/edit?usp=sharing>

[script]
function writeDataToFirebase() {
  var sheet = SpreadsheetApp.openById("1_BvT5diDqs2XiD1tvIDaTf_a5EBuOgvmQ2MgfDprU8I");
  var mysheet = sheet.setActiveSheet(sheet.getSheets()[0]);
  var data = mysheet.getDataRange().getValues();
  var dataToImport = {};
  
  
  var firebaseUrl = "https://vist-rfid.firebaseio.com/";
  var secret = "jDdGbaqymeSbpEgXAty5OHnOI47TDSEqhljxpzRg";
  var base = FirebaseApp.getDatabaseByUrl(firebaseUrl,secret);
  
 
  for(var i = 1; i < data.length; i++) {
    var ID = data[i][1];
    var TIME = data[i][2];
    var NUMBER = data[i][3];
    
    dataToImport[ID] = {
      ID : ID,
      TIME : TIME,
      NUMBER : NUMBER
    };
  }
 
  base.setData("rfid", dataToImport);
}
