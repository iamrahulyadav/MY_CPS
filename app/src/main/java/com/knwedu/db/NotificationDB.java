package com.knwedu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.knwedu.model.Notification;

import java.util.ArrayList;

/**
 * Created by ritwik.rai on 21/12/17.
 */

public class NotificationDB implements DBConstants {

    private static NotificationDB obj = null;


    public synchronized static NotificationDB obj() {

        if (obj == null)
            obj = new NotificationDB();
        return obj;

    }

    public Boolean saveHistoryData(Context context, ContentValues cv) {

        System.out.println(" ----------  ADD ROWS INTO NOTIFICATION TABLE --------- ");
        SQLiteDatabase mdb = CPSDBHelper.getInstance(context).getWritableDatabase();
        mdb.beginTransaction();
        try {
            mdb.insert(NOTIFICATION_TABLE, null, cv);
            mdb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mdb.endTransaction();
            return true;
        }

    }

    private Boolean isDatabaseEmpty(Cursor mCursor) {

        if (mCursor.moveToFirst()) {
            // NOT EMPTY
            return false;

        } else {
            // IS EMPTY
            return true;
        }

    }
    /*public void clearDBTables(Context mcContext) {

		System.out.println(" ----------  CLEAR BLOCK TABLES  --------- ");
		SQLiteDatabase mdb = DisaterManagementDatabase.getInstance(mcContext).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.delete(BLOCK_TABLE, null, null);
			mdb.delete(MPCS_PROJECT_TABLE, null, null);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mdb.endTransaction();
		}
	}*/

    public ArrayList<Notification> getOfflineNotifications(Context context) {

        ArrayList<Notification> historyArray = new ArrayList<Notification>();
        //		String[] columns = { _ID, MU_ID, THREAD_ID_HISTORY, IMAGE, LATITUDE, LONGITUDE, COMMENTS, KEYWORDS,
        //				ADDRESS, DATE, TIME, SCHOOL_CODE, VILLAGE_NAME, OTHER_DATA };

        SQLiteDatabase mdb = CPSDBHelper.getInstance(context).getReadableDatabase();
        /*Cursor cur = mdb.query(HISTORY_TABLE, columns, BLOCK_DISTRICT_ID + "=?" + "AND " + BLOCK_PROJ_TYPE + "=?", new String[] { districtId,
                projectType }, null, null, null);*/
        Cursor cur = mdb.query(NOTIFICATION_TABLE, null, null, null, null, null, null);

        if (!isDatabaseEmpty(cur)) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        Notification offlineSubmission = new Notification();
                        offlineSubmission.setTitle(cur.getString(cur.getColumnIndex(DBConstants.NOTIFICATION_TITLE)));
                        offlineSubmission.setMessage(cur.getString(cur.getColumnIndex(DBConstants.NOTIFICATION_MESSAGE)));
                        offlineSubmission.setRole(cur.getString(cur.getColumnIndex(DBConstants.ROLE)));
                        offlineSubmission.setModule(cur.getString(cur.getColumnIndex(DBConstants.MODULE)));
                        historyArray.add(offlineSubmission);
                    } while (cur.moveToNext());
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return historyArray;
    }


    /**
     * fetch members according to cityName
     * */
    /*public ArrayList<Member> getMembers(Context context, String cityName) {

        ArrayList<Member> members = new ArrayList<Member>();
        String[] columns = { _ID, CITY, ID, NAME, ID_NO, SPOUSE_NAME, CONTACT_NO, MOBILE, EMAIL, DESIGNATION, ADD1, ADD2, ADD3,
                PIN, TOWN, PIC };

        SQLiteDatabase mdb = CPSDBHelper.getInstance(context).getReadableDatabase();
        Cursor cur = mdb.query(MEMBERS_DIRECTORY_TABLE, columns, CITY + "=?", new String[] { cityName }, null, null, null);

		/*Cursor cur = mdb.query(HISTORY_TABLE, columns, BLOCK_DISTRICT_ID + "=?" + "AND " + BLOCK_PROJ_TYPE + "=?", new String[] { districtId,
				projectType }, null, null, null);*/
    //Cursor cur = mdb.query(HISTORY_TABLE, null, null, null, null, null, null);

        /*if (!isDatabaseEmpty(cur)) {
            try {
                if (cur.moveToFirst()) {
                    do {
                        Member member = new Member();
                        member.city = cur.getString(cur.getColumnIndex(DBConstants.CITY));
                        member.id = cur.getString(cur.getColumnIndex(DBConstants.ID));
                        member.name = cur.getString(cur.getColumnIndex(DBConstants.NAME));
                        member.idNo = cur.getString(cur.getColumnIndex(DBConstants.ID_NO));
                        member.spouseName = cur.getString(cur.getColumnIndex(DBConstants.SPOUSE_NAME));
                        member.contactNo = cur.getString(cur.getColumnIndex(DBConstants.CONTACT_NO));
                        member.mobile = cur.getString(cur.getColumnIndex(DBConstants.MOBILE));
                        member.email = cur.getString(cur.getColumnIndex(DBConstants.EMAIL));
                        member.designation = cur.getString(cur.getColumnIndex(DBConstants.DESIGNATION));
                        member.add1 = cur.getString(cur.getColumnIndex(DBConstants.ADD1));
                        member.add2 = cur.getString(cur.getColumnIndex(DBConstants.ADD2));
                        member.add3 = cur.getString(cur.getColumnIndex(DBConstants.ADD3));
                        member.pin = cur.getString(cur.getColumnIndex(DBConstants.PIN));
                        member.town = cur.getString(cur.getColumnIndex(DBConstants.TOWN));
                        member.picUrl = cur.getString(cur.getColumnIndex(DBConstants.PIC));
                        members.add(member);
                    } while (cur.moveToNext());
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return members;
    }*/
}
