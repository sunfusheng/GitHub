package com.sunfusheng.github.net.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private IDownloadListener downloadListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, IDownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (downloadListener != null) {
                    int percentage = (int) (totalBytesRead * 100 / contentLength());
                    if (bytesRead != -1) {
                        downloadListener.onProgress(totalBytesRead, contentLength(), percentage);
                    }

                }
                return bytesRead;
            }
        };
    }
}
