package demo.timeapp.aws;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Why no append - https://news.ycombinator.com/item?id=10746969
 */
@Service
public class AWSClient {

    private static final Logger LOG = LoggerFactory.getLogger(AWSClient.class);

    @Value("${aws.bucket.name}")
    private String existingBucketName;

    @Value("${aws.upload.dir}")
    private String uploaDir;

    public void upload (String filePath, String remotePath, CannedAccessControlList acl) {
        String uploadPath;
        if (acl == null)
            acl = CannedAccessControlList.Private;
        if (StringUtils.isNotBlank(uploaDir)) {
            uploadPath = uploaDir + remotePath;
        } else {
            uploadPath = remotePath;
        }
        LOG.info("S3: uploading, " + filePath + " to, " + remotePath);
        //AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.defaultClient();

        // new ProfileCredentialsProvider(), see credential providers.
        // http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
        AmazonS3 s3Client = new AmazonS3Client(new EnvironmentVariableCredentialsProvider());

        // Create a list of UploadPartResponse objects. You get one of these
        // for each part upload.
        List<PartETag> partETags = new ArrayList<PartETag>();

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new
                InitiateMultipartUploadRequest(existingBucketName, uploadPath).withCannedACL(acl);
        InitiateMultipartUploadResult initResponse =
                s3Client.initiateMultipartUpload(initRequest);

        File file = new File(filePath);
        long contentLength = file.length();
        long partSize = 5242880; // Set part size to 5 MB.

        try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(existingBucketName).withKey(uploadPath)
                        .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(
                        s3Client.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new
                    CompleteMultipartUploadRequest(
                    existingBucketName,
                    uploadPath,
                    initResponse.getUploadId(),
                    partETags);
            s3Client.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                    existingBucketName, uploadPath, initResponse.getUploadId()));
        }

    }

    public static void main(String[] args) throws IOException {
        String existingBucketName  = "dhvalm";
        String keyName             = "upload/test/hs_err_pid6942.log";
        String filePath            = "/Users/dhval/hs_err_pid6942.log";
        AWSClient client = new AWSClient();
        client.existingBucketName = existingBucketName;
        client.upload(filePath, keyName, CannedAccessControlList.PublicRead);
    }
}