// IMyService.aidl
package com.meizu.powertesttool.IPC;

import com.meizu.powertesttool.IPC.Student;

interface IMyService {
    List<Student> getStudent();
    void addStudent(in Student stu);
}
