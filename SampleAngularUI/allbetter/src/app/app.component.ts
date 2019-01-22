import { Component } from '@angular/core';
import { Tree } from '@angular/router/src/utils/tree';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'allbetter';

  hidden: boolean = false;
  fullScreen: boolean = false;
  topFlag: boolean = false;
  tintColor: string = '#108ee9';
  unselectedTintColor: string = '#888';
  tabbarStyle: object = { height: '600px' };

  ngOnInit() {
    this.fullScreen = true;
    this.tabbarStyle =  {
          position: 'fixed',
          height: '100%',
          width: '100%',
          top: 0
        }
  }


  showTabBar(event) {
    event.preventDefault();
    this.hidden = !this.hidden;
  }

  showFullScreen(event) {
    event.preventDefault();
    this.fullScreen = !this.fullScreen;
    this.tabbarStyle = this.fullScreen
      ? {
          position: 'fixed',
          height: '100%',
          width: '100%',
          top: 0
        }
      : { height: '600px' };
  }

  changePosition(event) {
    event.preventDefault();
    this.topFlag = !this.topFlag;
  }

  onPress(event) {
    console.log('event: ', event);
  }
}
