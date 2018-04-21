package com.knwedu.ourschool.utils;

import android.content.Context;
import android.util.Log;

import com.knwedu.constant.Url;
import com.knwedu.ourschool.vo.BlogBean;
import com.knwedu.ourschool.vo.BlogCommentsBean;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class JsonParser {

    Context context;

    // constructor
    public JsonParser() {

    }

    public JsonParser(Context context) {
        this.context = context;
    }

    public JSONObject getJSONFromUrlfrist(List<NameValuePair> namevaluepair, String urladd) {

        Log.e("urladd", "urladd"+urladd);
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        ServiceHandler sh = new ServiceHandler();
        json = sh.makeServiceCall(urladd, ServiceHandler.POST, namevaluepair);

        try {
            jObj = null;
            if (json != null)
                jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;

    }

    /***
     * Network Request
     * */
    public JSONObject getJSONFromUrlnew(List<NameValuePair> namevaluepair, String urladd) {

        JSONObject jObj = null;
        String json = "";
        // String url = Urls.base_url;
        String url = Url.BASE_URL;

        Log.e("Requesting URL", url + urladd);
        Log.e("CallingParams", "CallingParams: " + namevaluepair);
        if (url == null) {
            return jObj;
        }
        String HUrl = url + urladd;
        ServiceHandler sh = new ServiceHandler();
        json = sh.makeServiceCall(HUrl, ServiceHandler.POST, namevaluepair);
        Log.e("Response", "Response: " + json);

        try {
            jObj = null;
            if (json != null)
                jObj = new JSONObject(json);

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    public ArrayList<Object> getBlogData(String _jSONString) {
        JSONObject jSONObject;
        ArrayList<Object> blogList = new ArrayList<Object>();
        blogList.clear();

        try {
            jSONObject = new JSONObject(_jSONString);
            String result = jSONObject.getString("result");

            if (result.equalsIgnoreCase("1"))
                ;
            {
                JSONArray jArrayData = null;
                try {
                    jArrayData = jSONObject.getJSONArray("data");
                } catch (Exception ex) {
                    jArrayData = new JSONArray();
                }
                for (int i = 0; i < jArrayData.length(); i++) {
                    JSONObject jBlog = null;
                    try {
                        jBlog = jArrayData.getJSONObject(i);
                    } catch (Exception ex) {
                    }

                    BlogBean blogBean = new BlogBean();
                    String blog_id = "";
                    String title = "";
                    //String name="";
                    String description = "";
                    String createdBy = "";
                    String createdAt = "";
                    String no_comments = "";
                    String image = "";
                    String isComment = "0";


                    ArrayList<BlogCommentsBean> commentList = new ArrayList<BlogCommentsBean>();
                    ;
                    commentList.clear();

                    try {
                        blog_id = jBlog.getString("id");
                    } catch (Exception ex) {
                    }
                    try {
                        title = jBlog.getString("blog_title");
                    } catch (Exception ex) {
                    }
                    try {
                        description = jBlog.getString("blog_description");
                    } catch (Exception ex) {
                    }
                    try {
                        //------------edited by Moumita--------
                        createdBy = jBlog.getString("fname") + " "
                                + jBlog.getString("lname");
                        //createdBy=jBlog.getString("name");
                    } catch (Exception ex) {
                    }
                    try {
                        createdAt = jBlog.getString("created_date");
                    } catch (Exception ex) {
                    }
                    try {
                        no_comments = jBlog.getString("total");
                    } catch (Exception ex) {
                    }
                    try {
                        image = jBlog.getString("image");
                    } catch (Exception ex) {
                    }
                    try {
                        isComment = jBlog.getString("is_comment");
                    } catch (Exception ex) {
                    }

                    blogBean.setBlogId(blog_id);
                    blogBean.setCreatedAt(createdAt);
                    blogBean.setCreatedBy(createdBy);
                    blogBean.setDescription(description);

                    blogBean.setTitle(title);
                    blogBean.setNo_comments(no_comments);
                    blogBean.setImage(image);
                    blogBean.setIsComment(isComment);

                    JSONArray jArrayComments = null;
                    try {
                        jArrayComments = jBlog.getJSONArray("comment_info");
                    } catch (Exception ex) {
                        jArrayComments = new JSONArray();
                    }
                    for (int j = 0; j < jArrayComments.length(); j++) {
                        JSONObject jBlogComment = null;
                        try {
                            jBlogComment = jArrayComments.getJSONObject(j);
                        } catch (Exception ex) {
                        }

                        BlogCommentsBean blogComments = new BlogCommentsBean();
                        String name = "";
                        String commentCreatedAt = "";
                        String comment = "";
                        String commentid = "";
                        String createdby = "";
                        try {
                            name = jBlogComment.getString("fname") + " "
                                    + jBlogComment.getString("lname");
                        } catch (Exception ex) {
                        }
                        try {
                            comment = jBlogComment.getString("comment");
                        } catch (Exception ex) {
                        }
                        try {
                            commentid = jBlogComment.getString("comment_id");
                        } catch (Exception ex) {
                        }
                        try {
                            createdby = jBlogComment.getString("created_by");
                        } catch (Exception ex) {
                        }
                        try {
                            commentCreatedAt = jBlogComment
                                    .getString("created_date");
                        } catch (Exception ex) {
                        }

                        blogComments.setComment(comment);
                        blogComments.setCreatedAt(commentCreatedAt);
                        blogComments.setName(name);
                        blogComments.setcommentid(commentid);
                        blogComments.setcreatedby(createdby);
                        commentList.add(blogComments);
                    }
                    blogBean.setCommentList(commentList);
                    blogList.add(blogBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blogList;

    }


    /**
     * Allows you to trust certificates from additional KeyStores in addition to
     * the default KeyStore
     */
    public class AdditionalKeyStoresSSLSocketFactory extends SSLSocketFactory {
        protected SSLContext sslContext = SSLContext.getInstance("TLS");

        public AdditionalKeyStoresSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(null, null, null, null, null, null);
            sslContext.init(null, new TrustManager[]{new AdditionalKeyStoresTrustManager(keyStore)}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }


        /**
         * Based on http://download.oracle.com/javase/1.5.0/docs/guide/security/jsse/JSSERefGuide.html#X509TrustManager
         */
        public class AdditionalKeyStoresTrustManager implements X509TrustManager {

            protected ArrayList<X509TrustManager> x509TrustManagers = new ArrayList<X509TrustManager>();


            protected AdditionalKeyStoresTrustManager(KeyStore... additionalkeyStores) {
                final ArrayList<TrustManagerFactory> factories = new ArrayList<TrustManagerFactory>();

                try {
                    // The default Trustmanager with default keystore
                    final TrustManagerFactory original = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    original.init((KeyStore) null);
                    factories.add(original);

                    for (KeyStore keyStore : additionalkeyStores) {
                        final TrustManagerFactory additionalCerts = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        additionalCerts.init(keyStore);
                        factories.add(additionalCerts);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }



            /*
             * Iterate over the returned trustmanagers, and hold on
             * to any that are X509TrustManagers
             */
                for (TrustManagerFactory tmf : factories)
                    for (TrustManager tm : tmf.getTrustManagers())
                        if (tm instanceof X509TrustManager)
                            x509TrustManagers.add((X509TrustManager) tm);


                if (x509TrustManagers.size() == 0)
                    throw new RuntimeException("Couldn't find any X509TrustManagers");

            }

            /*
             * Delegate to the default trust manager.
             */
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                final X509TrustManager defaultX509TrustManager = x509TrustManagers.get(0);
                defaultX509TrustManager.checkClientTrusted(chain, authType);
            }

            /*
             * Loop over the trustmanagers until we find one that accepts our server
             */
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                for (X509TrustManager tm : x509TrustManagers) {
                    try {
                        tm.checkServerTrusted(chain, authType);
                        return;
                    } catch (CertificateException e) {
                        // ignore
                    }
                }
                throw new CertificateException();
            }

            public X509Certificate[] getAcceptedIssuers() {
                final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
                for (X509TrustManager tm : x509TrustManagers)
                    list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
                return list.toArray(new X509Certificate[list.size()]);
            }
        }

    }
}