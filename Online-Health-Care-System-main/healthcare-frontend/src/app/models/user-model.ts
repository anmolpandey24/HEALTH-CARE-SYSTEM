export interface User {
    id?:number;
    username:string;
    email:string;
    password?:string;
    role?:string;
    active?:boolean
}

export interface RegisterRequest {
    username:string;
    email:string;
    password:string;
}

export interface LoginRequest {
    username:string;
    password:string;
}

export interface LoginResponse {
    token: string;
    role: string;
    username: string;
}

export interface ChangePasswordRequest {
    username: string;
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
}

export interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
}

export interface Appointment {
    id?: number;
    userId?: number;
    centerId?: number;
    testId?: number;
    slotId?: number;
    status?: string;
    appointmentDate?: string;
    appointmentTime?: string;
    userName?: string;
    centerName?: string;
    testName?: string;
    testPrice?: number;
}

export interface AppointmentRequest {
    centerId: number;
    testId: number;
    appointmentDate: string;
    slot: string;
}

export interface DiagnosticCenter {
    id?: number;
    centerName: string;
    address?: string;
    location?: string;
    ownerName?: string;
    adminUsername?: string;
    status?: string;
    createdDate?: string;
    user?: { username?: string };
}

export interface CenterRequest {
    centerName: string;
    address: string;
}

export interface DiagnosticTest {
    id?: number;
    testName: string;
    description?: string;
    testPrice: number;
    duration?: number;
}

export interface DiagnosticTestRequest {
    testName: string;
    description: string;
    testPrice: number;
}

export interface CenterTest {
    id?: number;
    centerId?: number;
    testId?: number;
    maxBooking?: number;
    centerName?: string;
    testName?: string;
    testPrice?: number;
    test?: DiagnosticTest;
}

export interface CenterTestRequest {
    testId: number;
    maxBooking: number;
}

export interface Slot {
    id?: number;
    centerTestId?: number;
    slotDate?: string;
    slotTime?: string;
    available?: boolean;
}

export interface Report {
    id?: number;
    appointmentId?: number;
    patientName: string;
    fileName?: string;
    fileType?: string;
    uploadedDate?: string;
}