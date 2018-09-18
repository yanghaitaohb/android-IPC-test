package com.yht.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Observable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yht.ipc.databinding.ActivityMainBinding;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    IBookManager iBookManager;
    List<Book> books;
    StringBuilder sBuilder;
    ObservableInt lastId;
    ObservableField<String> display;
    ObservableField<String> inputName;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final EditText editText = (EditText) findViewById(R.id.editText);
//        final TextView textView = (TextView) findViewById(R.id.tv);
        lastId = new ObservableInt();
        display = new ObservableField<>();
        inputName = new ObservableField<>();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLastId(lastId);
        binding.setDisplay(display);
        binding.setInputName(inputName);
        binding.setTime(new Date());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iBookManager != null) {
                    if (!TextUtils.isEmpty(inputName.get())) {
                        try {
                            iBookManager.addBook(new Book(inputName.get()));
                            books = iBookManager.getBookList();
                            if (books != null) {
                                sBuilder = new StringBuilder();
                                for (Book b : books) {
                                    sBuilder.append(b.bookId).append(" : ").append(b.bookName).append("\n");
                                }
                                display.set(sBuilder.toString());
                                int size = books.size();
                                if (size != 0) {
                                    lastId.set((int)books.get(size - 1).bookId);
                                }
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "不可为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "等待连接", Toast.LENGTH_SHORT).show();
                    connectServer();
                }
            }
        });
        connectServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectServer();
    }

    private void connectServer() {
        if (iBookManager == null) {
            Intent intent = new Intent("com.yht.pic_server");
            intent.setPackage("com.yht.ipc_server");
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iBookManager = IBookManager.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iBookManager = null;
        }
    };
}
