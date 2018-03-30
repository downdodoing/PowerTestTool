package com.meizu.powertesttool.downloadTest;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.meizu.powertesttool.downloadTest.interfaceI.IDownloadListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

/**
 * 实现断点下载
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCESS = 0;//下载成功
    public static final int TYPE_FAILED = 1;//下载失败
    public static final int TYPE_PAUSED = 2;//下载暂停
    public static final int TYPE_CANCELED = 3;//下载取消

    public static int mPerLengthSeconds;

    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;//最新的进度

    private static DownloadTask mDownload;
    private IDownloadListener mListener;

    public DownloadTask(IDownloadListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 在线程池中执行，用于执行较为耗时的操作
     *
     * @param params
     * @return
     */
    @Override
    protected Integer doInBackground(String... params) {
        InputStream inputStream = null;
        RandomAccessFile saveFile = null;

        File file = null;
        try {
            long downloadLength = 0;//用于记录已经下载的文件长度
            String downloadUrl = params[0];

            file = getFile(downloadUrl);

            if (file.exists()) {
                downloadLength = file.length();
            }
            //获取文件的总大小
            long contentLength = getContentLength(downloadUrl);
            if (0 == contentLength) {
                return TYPE_FAILED;
                //为了实现不间断下载，当文件已经下载成功后则将原文件进行删除，重新下载
            } else if (contentLength <= downloadLength) {
                file.delete();
                downloadLength = 0;
            }

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(500, TimeUnit.SECONDS);
            client.setConnectTimeout(500, TimeUnit.SECONDS);

            Request request = new Request.Builder()
                    //确定下载的范围
                    .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();

            long lastDownload = 0;
            long currenTime = System.currentTimeMillis();
            long lastTime;

            if (null != request) {
                inputStream = response.body().byteStream();
                //如果此文件不存在，则会自动创建
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downloadLength);
                byte[] b = new byte[1024];

                int total = 0;
                int len;
                while ((len = inputStream.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        lastTime = System.currentTimeMillis();

                        long length = (total - lastDownload) / 1024;
                        if (length >= mPerLengthSeconds) {
                            lastDownload = total;
                            long time = lastTime - currenTime;
                            Log.i("OkHttpClient'", "doInBackground: length=" + length + " mPerLengthSeconds=" + mPerLengthSeconds + " time=" + time);

                            /*
                             *当获取到50kb 100kb 1024kb的时间小于1秒的时候,就进行沉睡
                             * 也就是获取数据的时间如果小于1秒那么沉睡的时间就等于1秒-获取数据的时间
                             */
                            if (time < 1000) {
                                try {
                                    Thread.sleep(1000 - time);
                                    Log.i("OkHttpClient 时间", "doInBackground: " + (1000 - time));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            currenTime = lastTime;
                        }

                        saveFile.write(b, 0, len);
                        int progress = (int) ((total + downloadLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
            }
            response.body().close();
            return TYPE_SUCESS;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != saveFile) {
                    saveFile.close();
                }
                if (isCanceled && null != file) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return TYPE_FAILED;
    }

    public static File getFile(String downloadUrl) {
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directory + fileName);

        return file;
    }

    /**
     * 调用publishProgress后被调用用于更新进度条
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            mListener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /**
     * doInBackground方法执行完之后立即被执行
     *
     * @param result
     */
    @Override
    protected void onPostExecute(Integer result) {
        switch (result) {
            case TYPE_SUCESS:
                mListener.onSuccess();
                break;
            case TYPE_FAILED:
                mListener.onFailed();
                break;
            case TYPE_PAUSED:
                mListener.onPause();
                break;
            case TYPE_CANCELED:
                mListener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = okHttpClient.newCall(request).execute();

        if (null != response && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }

        return 0;
    }

}
