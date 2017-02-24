package com.ownchan.providerdemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener {

    private ContentResolver resolver;
    private ListView listView;

    private static final String AUTHORITY = "com.owenchan.provider.PersonProvider";
    private static final Uri PERSON_ALL_URI = Uri.parse("content://" + AUTHORITY + "/persons");

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //update records.
            requery();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resolver = getContentResolver();
        listView = (ListView) findViewById(R.id.list_view);
        getContentResolver().registerContentObserver(PERSON_ALL_URI, true, new PersonObserver(handler));
        initView();
    }

    /**
     * 初始化点击的button
     */
    private void initView() {
        findViewById(R.id.init).setOnClickListener(this);
        findViewById(R.id.query).setOnClickListener(this);
        findViewById(R.id.insert).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.sp).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.init:
                init();
                break;
            case R.id.query:
                query();
                break;
            case R.id.insert:
                insert();
                break;
            case R.id.update:
                update();
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.sp:
                insertSp();
            default:
                //do nothing
        }

    }


    /**
     * 初始化
     */
    public void init() {
        ArrayList<Person> persons = new ArrayList<Person>();

        Person person1 = new Person("Ella", 22, "lively girl");
        Person person2 = new Person("Jenny", 22, "beautiful girl");
        Person person3 = new Person("Jessica", 23, "sexy girl");
        Person person4 = new Person("Kelly", 23, "hot baby");
        Person person5 = new Person("Jane", 25, "pretty woman");

        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);
        persons.add(person5);

        for (Person person : persons) {
            ContentValues values = new ContentValues();
            values.put("name", person.name);
            values.put("age", person.age);
            values.put("info", person.info);
            resolver.insert(PERSON_ALL_URI, values);
        }
    }

    private void insertSp() {
            SharedPreferences pref = MainActivity.this.getSharedPreferences("owen",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirstInput", true);
            editor.putString("age","18");
            editor.putString("name","owen");
            editor.commit();
    }


    /**
     * 查询所有记录
     */
    public void query() {
        Cursor cursor = resolver.query(PERSON_ALL_URI, null, null, null, null);


        CursorWrapper cursorWrapper = new CursorWrapper(cursor) {

            @Override
            public String getString(int columnIndex) {
                //将简介前加上年龄
                if (getColumnName(columnIndex).equals("info")) {
                    int age = getInt(getColumnIndex("age"));
                    return age + " years old, " + super.getString(columnIndex);
                }
                return super.getString(columnIndex);
            }
        };

        //Cursor须含有"_id"字段
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                cursorWrapper, new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
        startManagingCursor(cursor);
    }

    /**
     * 插入一条记录
     */
    public void insert() {
        Person person = new Person("Alina", 26, "attractive lady");
        ContentValues values = new ContentValues();
        values.put("name", person.name);
        values.put("age", person.age);
        values.put("info", person.info);
        resolver.insert(PERSON_ALL_URI, values);
    }

    /**
     * 更新一条记录
     */
    public void update() {
        Person person = new Person();
        person.name = "Jane";
        person.age = 30;
        //将指定name的记录age字段更新为30
        ContentValues values = new ContentValues();
        values.put("age", person.age);
        resolver.update(PERSON_ALL_URI, values, "name = ?", new String[]{person.name});

        //将_id为1的age更新为30
//      Uri updateUri = ContentUris.withAppendedId(PERSON_ALL_URI, 1);
//      resolver.update(updateUri, values, null, null);
    }

    /**
     * 删除一条记录
     */
    public void delete() {
        //删除_id为1的记录
        Uri delUri = ContentUris.withAppendedId(PERSON_ALL_URI, 1);
        resolver.delete(delUri, null, null);

        //删除所有记录
//      resolver.delete(PERSON_ALL_URI, null, null);
    }

    /**
     * 重新查询
     */
    private void requery() {
        //实际操作中可以查询集合信息后Adapter.notifyDataSetChanged();
        query();
    }


}
