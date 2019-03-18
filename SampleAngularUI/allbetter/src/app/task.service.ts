import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  randomUserUrl = 'http://localhost:8082';

  constructor(private http: HttpClient) { }

  findDailyTask(date:String): Observable<{}> {
    let params = new HttpParams()
    .append('date', `${date}`);
    console.log(params)
    return this.http.get(`${this.randomUserUrl}/findDailyTask`, {
      params
    });
  }

  findUser(): Observable<{}> {
    let params = new HttpParams();
    
    return this.http.get(`${this.randomUserUrl}/findUser`, {
      params
    });
  }

  saveDailyTask(tasks): Observable<{}> {
    let params = new HttpParams().append('id', `${tasks.id}`)
    .append('date', `${tasks.date}`)
    .append('taskId', `${tasks.taskId}`)
    .append('taskName', `${tasks.taskName}`)
    .append('targetUser', `${tasks.targetUser}`)
    .append('checked', `${tasks.checked}`);
    console.log(params)
    return this.http.get(`${this.randomUserUrl}/saveDailyTask`, {
      params
    });
  }

}

