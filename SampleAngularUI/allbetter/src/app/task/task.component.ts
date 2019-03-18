import { Component, OnInit } from '@angular/core';
import { TaskService } from '../task.service';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {

  accordions: Array<any> = [
    { title: '千千', child: ['content 1', 'content 1', 'content 1'] },
    { title: '杰杰', child: ['content 2', 'content 2', 'content 2'], inactive: false }
  ];

  checkItemListData = [
    { value: 0, name: '好好吃饭', checked: false },
    { value: 1, name: '尊敬长辈', checked: false },
    { value: 2, name: '有礼貌', checked: false }
  ];

  activeKey = [0, 1];

  constructor(private taskService: TaskService) { }

  ngOnInit() {
    console.log(this.taskDate);
    this.initData(this.currentDateFormat(this.taskDate, 'yyyymmdd'))
  }

  // locale = en_US;
  //name2 = '选择';
  

  nowTimeStamp = Date.now();
  now = new Date(this.nowTimeStamp);
  utcNow = new Date(this.now.getTime() + this.now.getTimezoneOffset() * 60000);

  value = [];
  taskDate = new Date();

  dailyTasks = [];
  users = [];
  loading = false;

  findDailyTask(date:String): void {

    this.taskService.findDailyTask(date).subscribe((data: any) => {
      this.loading = true;
      this.dailyTasks = data;
      console.log(this.dailyTasks);
    });
  }

  findUser(): void {

    this.taskService.findUser().subscribe((data: any) => {
      this.loading = true;
      this.users = data;
      console.log(this.users);
    });
  }

  saveDailyTask(task): void {

    this.taskService.saveDailyTask(task).subscribe((data: any) => {
      this.loading = false;
      console.log(data);
    });
  }

  initData(date:String): void {
    this.findUser();
    this.findDailyTask(date);
    
  }

  currentDateFormat(date, format: string = 'yyyy-mm-dd HH:MM'): any {
    const pad = (n: number): string => (n < 10 ? `0${n}` : n.toString());
    return format
      .replace('yyyy', date.getFullYear())
      .replace('mm', pad(date.getMonth() + 1))
      .replace('dd', pad(date.getDate()))
      .replace('HH', pad(date.getHours()))
      .replace('MM', pad(date.getMinutes()))
      .replace('ss', pad(date.getSeconds()));
  }

  onChange(event) {
    console.log(event);
  }

  onDateChange(result) {
    //this.taskDate = this.currentDateFormat(result, 'yyyymmdd');
    this.taskDate = result;
    console.log(this.taskDate);
    this.initData(this.currentDateFormat(this.taskDate, 'yyyymmdd'))
  }

  onDailyTaskStatusChange(result) {
    //this.taskDate = this.currentDateFormat(result, 'yyyymmdd');
    console.log(result.value);
    this.saveDailyTask(result.value);
  }

  formatIt(date: Date, form: string) {
    const pad = (n: number) => (n < 10 ? `0${n}` : n);
    const dateStr = `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
    const timeStr = `${pad(date.getHours())}:${pad(date.getMinutes())}`;
    if (form === 'YYYY-MM-DD') {
      return dateStr;
    }
    if (form === 'HH:mm') {
      return timeStr;
    }
    return `${dateStr} ${timeStr}`;
  }

}
