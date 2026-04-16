import { Injectable, inject } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpInterceptorFn,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth-service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    if (request.url.includes('/api/centers') || request.url.includes('/api/auth')) {
      console.log('JwtInterceptor request:', request.method, request.url, 'token present:', !!token);
    }

    // Don't add token for auth endpoints that don't require authentication
    const isAuthEndpoint = request.url.includes('/api/auth/login') ||
                          request.url.includes('/api/auth/register-user') ||
                          request.url.includes('/api/auth/register-admin') ||
                          request.url.includes('/api/otp/send') ||
                          request.url.includes('/api/otp/verify');

    if (token && !isAuthEndpoint) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(request);
  }
}

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (req.url.includes('/api/centers') || req.url.includes('/api/auth')) {
    console.log('JwtInterceptor request:', req.method, req.url, 'token present:', !!token);
  }

  // Don't add token for auth endpoints that don't require authentication
  const isAuthEndpoint = req.url.includes('/api/auth/login') ||
                        req.url.includes('/api/auth/register-user') ||
                        req.url.includes('/api/auth/register-admin') ||
                        req.url.includes('/api/otp/send') ||
                        req.url.includes('/api/otp/verify');

  if (token && !isAuthEndpoint) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(req);
};
