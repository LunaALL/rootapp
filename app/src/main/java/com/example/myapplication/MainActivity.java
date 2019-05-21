package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    int mMainValue=0;
    TextView mMainText;
    TextView mBackText;
    TextView mTextq;//팩토,피보나치용
    EditText mNumEdit;
    CalcThread mThread;
    Factori mThread1;
    Fibo mThread2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMainText=(TextView)findViewById(R.id.mainvalue);
        mBackText=(TextView)findViewById(R.id.backvalue);
        mTextq=(TextView)findViewById(R.id.Facto);
        mNumEdit=(EditText)findViewById(R.id.number);

        mThread=new CalcThread(mHandler);
        mThread1=new Factori(mHandler);
        mThread2=new Fibo(mHandler);
        mThread.setDaemon(true);
        mThread.start();
        mThread1.start();
        mThread2.start();
    }

    public void mOnClick(View v){
        Message msg;
        switch(v.getId()){
            case R.id.increase:
            mMainValue++;
            mMainText.setText("MainValue:"+mMainValue);
            break;
            case R.id.square:
            msg=new Message();
            msg.what=0;
            msg.arg1=Integer.parseInt(mNumEdit.getText().toString());
            mThread.mBackHandler.sendMessage(msg);
            break;
            case R.id.root:
            msg=new Message();
            msg.what=1;
            msg.arg1=Integer.parseInt(mNumEdit.getText().toString());
            mThread.mBackHandler.sendMessage(msg);
            break;
            case R.id.factorial:
            msg=new Message();
            msg.what=2;
            msg.arg1=Integer.parseInt(mNumEdit.getText().toString());//똑같이 mNumEdit에서 값을입력받는다.
            mThread1.mBackHandler.sendMessage(msg);
            break;
            case R.id.Fibonacci:
                msg=new Message();
                msg.what=3;
                msg.arg1=Integer.parseInt(mNumEdit.getText().toString());
                mThread2.mBackHandler.sendMessage(msg);
                break;


        }
    }

    Handler mHandler=new Handler(){//값을표시하는핸들러입니다.
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:

                    mBackText.setText("SquareResult:"+msg.arg1);
                    mTextq.setText("");//피보,팩토용텍뷰초기화.
                break;

                case 1:

                    mBackText.setText("RootResult:"+((Double)msg.obj).doubleValue());
                    mTextq.setText("");//피보,팩토용텍뷰초기화.
                break;

                case 2:
                    mTextq.setText("");//피보,팩토용텍뷰초기화.
                    for(int i=1;i<msg.arg1;i++){
                    mTextq.append(i+"*");
                }
                    mTextq.append(""+msg.arg1);
                    mBackText.setText("factorial:"+msg.obj);
                break;

                case 3:
                    mTextq.setText("");//피보,팩토용텍뷰초기화.
                    int a=1,b=1,sum=2;
                    if(msg.arg2!=1){mTextq.append("1 + 1"); }

                    for(int i=2;i<msg.arg2;i++){
                        sum=a+b;
                        a=b;
                        b=sum;
                        mTextq.append(" + "+ sum);
                    }
                    mBackText.setText("pibonacci  수열에서 "+msg.arg2+" 번째의 값은 " + +msg.arg1 );
                    break;


            }
        }
    };
}

class CalcThread extends Thread{//연산쓰레드1.제곱,제곱근구하기.
        Handler mMainHandler;
        Handler mBackHandler;
        CalcThread(Handler handler){
        mMainHandler=handler;
        }

        public void run(){
        Looper.prepare();
        mBackHandler=new Handler(){
        public void handleMessage(Message msg){
        Message retmsg=new Message();
        switch(msg.what){
        case 0:
        try{ Thread.sleep(200);} catch (InterruptedException e){;}
        retmsg.what=0;
        retmsg.arg1=msg.arg1*msg.arg1;
        break;
        case 1:
            try{ Thread.sleep(200);} catch (InterruptedException e){;}
        retmsg.what=1;
        retmsg.obj=new Double(Math.sqrt((double)msg.arg1));
        break;
        }
        mMainHandler.sendMessage(retmsg);
        }
        };
        Looper.loop();
        }
        }

class Factori extends Thread{//연산스레드2,팩토리얼구하기.
    Handler mMainHandler;
    Handler mBackHandler;

    Factori(Handler handler){mMainHandler=handler;}

    public void run(){
        Looper.prepare();
        mBackHandler=new Handler(){
            public void handleMessage(Message msg){
                Message Num=new Message();
                try{
                    Thread.sleep(200);
                }catch(InterruptedException e){;}
                Num.what=2;
                int fac=1;
                for(int i=1;i<msg.arg1;i++){
                    fac=fac*(i+1);
                }
                Num.arg1=msg.arg1;
                Num.obj=fac;
                mMainHandler.sendMessage(Num);

            }
        };
        Looper.loop();
    }
}


class Fibo extends Thread{//연산스레드2,팩토리얼구하기.
    Handler mMainHandler;
    Handler mBackHandler;

    Fibo(Handler handler){mMainHandler=handler;}

    public void run(){
        Looper.prepare();
        mBackHandler=new Handler(){
            public void handleMessage(Message msg){
                Message Num=new Message();
                try{
                    Thread.sleep(200);
                }catch(InterruptedException e){;}
                int a=1; int b=1;
                int sum=0;
                if(msg.arg1==1 || msg.arg1==2){
                    sum=1;
                }
                for(int i=2; i<msg.arg1;i++){
                    sum=a+b;
                    a=b;
                    b=sum;
                }
                Num.what=3;
                Num.arg1=sum;
                Num.arg2=msg.arg1;
                mMainHandler.sendMessage(Num);

            }
        };
        Looper.loop();
    }
}


