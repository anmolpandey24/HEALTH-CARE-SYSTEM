import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupAdmin } from './signup-admin';

describe('SignupAdmin', () => {
  let component: SignupAdmin;
  let fixture: ComponentFixture<SignupAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignupAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(SignupAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
