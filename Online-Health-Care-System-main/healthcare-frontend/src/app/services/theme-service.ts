import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private darkMode=false;
  constructor() {
    const savedTheme = localStorage.getItem('theme');

    if(savedTheme === 'dark') {
      this.enableDark();
    }
    else{
      this.enableLight();
    }
  }

  toggletheme() {
    this.darkMode=!this.darkMode;

    if(this.darkMode) {
      this.enableDark();
    }
    else{
      this.enableLight();
    }
  }

  enableDark() {
    document.documentElement.classList.add('dark');
    localStorage.setItem('theme', 'dark');
    this.darkMode=true;

  }

  enableLight() {
    document.documentElement.classList.remove('dark');
    localStorage.setItem('theme', 'light');
    this.darkMode=false;
  }
  isDarkMode() { 
    return this.darkMode;
  }
}
