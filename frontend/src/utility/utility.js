export default { 
  formatDate (date) {
    //pretvara datum u string formata dd.MM.yyy hh:mm
    if(!date)
      return null;
    let temp = new Date(date);
    let day = temp.getDate();
    let month = temp.getMonth() + 1;
    let hour = temp.getHours();
    let minute = temp.getMinutes();
    if((String(day)).length==1)
      day='0'+day;
    if((String(month)).length==1)
      month='0'+month;
    if((String(hour)).length==1)
      hour='0'+hour;
    if((String(minute)).length==1)
      minute='0'+minute;
    return `${day}.${month}.${temp.getFullYear()} ${hour}:${minute}`;
  },
  stringToDate(value){
    //pretvara string formata dd.MM.yyy hh:mm u datum
    let date = new Date();
    let temp = value.split(' ');
    let temp1 = temp[0].split('.');
    let temp2 = temp[1].split(':');
    date.setDate(temp1[0]);
    date.setMonth(temp1[1] - 1);
    date.setFullYear(temp1[2]);
    date.setHours(temp2[0]);
    date.setMinutes(temp2[1]);
    date.setSeconds(0);
    date.setMilliseconds(0);
    return date;
  },

  handleTimeZone(date){
    //umanjuje dva sata od datuma usled razlicite vremenske zone dobijene sa servera
    date.setMinutes(date.getMinutes() + date.getTimezoneOffset());
    return date;
  }
};