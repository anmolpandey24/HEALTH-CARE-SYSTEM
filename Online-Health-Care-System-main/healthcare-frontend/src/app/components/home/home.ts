import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [RouterLink, CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent {
  features = [
    {
      icon: '🏥',
      title: 'Find Centers',
      description: 'Browse and discover trusted diagnostic centers near you',
    },
    {
      icon: '📋',
      title: 'Book Tests',
      description: 'Schedule diagnostic tests conveniently online',
    },
    {
      icon: '📊',
      title: 'View Reports',
      description: 'Access your test reports securely anytime',
    },
    {
      icon: '👨‍⚕️',
      title: 'Expert Care',
      description: 'Get guidance from qualified healthcare professionals',
    },
  ];

  testimonials = [
    {
      name: 'Rahul Sharma',
      role: 'Patient',
      text: 'Very convenient service. Booked my appointment in just 2 minutes!',
      avatar: 'RS',
    },
    {
      name: 'Dr. Priya Patel',
      role: 'Diagnostic Center',
      text: 'Great platform to manage our center and appointments efficiently.',
      avatar: 'PP',
    },
    {
      name: 'Anjali Singh',
      role: 'Patient',
      text: 'Quick report delivery and excellent customer support.',
      avatar: 'AS',
    },
  ];
}
