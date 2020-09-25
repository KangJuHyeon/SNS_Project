package com.example.sns_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.sns_project.R;
import com.example.sns_project.fragment.HomeFragment;
import com.example.sns_project.fragment.UserInfoFragment;
import com.example.sns_project.fragment.UserListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends BasicActivity {
    // FirebaseAuth auth= FirebaseAuth.getInstance();  // 파이어베이스 로그아웃 할 경우 환경설정
    private static final String TAG = "MainActivity";
    private  long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarTitle(getResources().getString(R.string.app_name));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();                                      // 뒤로가기 및 종료
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid()); // 액티비티를 모두 죽인다. 프로세스 강제 종료
        System.exit(1);
    }

    // 로그 아웃을 만들기 위한 메뉴를 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   // 메뉴를 만들어주는 코드
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // 메뉴 안에 옵션을 이용한 LogOut 구현
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {     // 메뉴 안에 옵션 선택을 이용한 코드

        switch (item.getItemId()) {

            case R.id.logout: {
                FirebaseAuth.getInstance().signOut(); // 파이어베이스 로그 아웃 구현 코드
                myStartActivity(LoginActivity.class); // 로그아웃 할 경우 어느 액티비티로 이동할지 선언
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                init();
                break;
        }
    }

    private void init(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            myStartActivity(SignUpActivity.class);
        } else {
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commit();

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            HomeFragment homeFragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, homeFragment)
                                    .commit();
                            return true;
                        case R.id.myInfo:
                            UserInfoFragment userInfoFragment = new UserInfoFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, userInfoFragment)
                                    .commit();
                            return true;
                        case R.id.userList:
                            UserListFragment userListFragment = new UserListFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, userListFragment)
                                    .commit();
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    private void myStartActivity(Class c) {                        // intent 액티비티 이것을 이용해 화면전환을 사용
        Intent intent = new Intent(this, c);        // 명시적 intent 사용 어느 액티비로 갈지 선언
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);          // 뒤로가기 버튼을 사용하면 지금 사용하는 액티비티를 초기화
        startActivityForResult(intent, 1);            // 다른 액티비티 호출
    }
}