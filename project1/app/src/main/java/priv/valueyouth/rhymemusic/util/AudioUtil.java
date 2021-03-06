package priv.valueyouth.rhymemusic.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import priv.valueyouth.rhymemusic.activity.MotionAnalysis;

import android.util.Log;
/**
 * 该类会返回一个带有音乐信息的List
 * Created by Idea on 2016/5/4.
 */
public class AudioUtil
{
    private static final String TAG = "RhymeMusic";
    private static final String SUB = "[AudioUtil]#";

    /*定义与音乐相关的信息集合*/
    public static final String[] AUDIO_KEYS = new String[]{
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TITLE_KEY,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_KEY,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_KEY,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATA,

            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,

            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_PODCAST,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.IS_NOTIFICATION,
    };

    /**
     * 返回一个带有音乐信息的List
     * @param context 环境参数
     * @return 返回一个带有音乐信息的List
     */
    public static ArrayList<Audio> getAudioList(Context context)
    {
        ArrayList<Audio> audioList = new ArrayList<Audio>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                AUDIO_KEYS, null, null, null);

        for ( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() )
        {
            Bundle bundle = new Bundle();

            for ( int i = 0; i < AUDIO_KEYS.length; i++ )
            {
                final String key = AUDIO_KEYS[i];
                final int colIndex = cursor.getColumnIndex(key);
                final int type = cursor.getType(colIndex);

                switch (type)
                {
                    case Cursor.FIELD_TYPE_BLOB:
                        break;

                    case Cursor.FIELD_TYPE_FLOAT:
                        float fValue = cursor.getFloat(colIndex);
                        bundle.putFloat(key, fValue);
                        break;

                    case Cursor.FIELD_TYPE_INTEGER:
                        int iValue = cursor.getInt(colIndex);
                        bundle.putInt(key, iValue);
                        break;

                    case Cursor.FIELD_TYPE_NULL:
                        break;

                    case Cursor.FIELD_TYPE_STRING:
                        String sValue = cursor.getString(colIndex);
                        if (key == "title") {
                            int len_t=sValue.length();
                            for (String s : MotionAnalysis.MOODS) {
                                int index = sValue.indexOf(s);

                                if (index >= 0) {
                                    bundle.putString("_mood", s);
                                    int len = s.length();
                                    String title = sValue.substring(0,len_t-len );
                                    sValue=title;
                                    Log.d("INFO", "CHECKING......" + s);
                                    break;
                                }

                            }
                        }
                        bundle.putString(key, sValue);
                        break;
                }
            }
//            String title=bundle.getString(MediaStore.Audio.Media.TITLE);//added


            Audio audio = new Audio(bundle);
            audioList.add(audio);
        }

        cursor.close();
        return audioList;
    }

}
