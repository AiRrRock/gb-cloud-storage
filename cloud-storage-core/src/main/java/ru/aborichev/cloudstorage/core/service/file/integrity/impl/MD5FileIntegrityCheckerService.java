package ru.aborichev.cloudstorage.core.service.file.integrity.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.service.file.integrity.FileIntegrityCheckerService;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5FileIntegrityCheckerService implements FileIntegrityCheckerService {
    private MessageDigest md;
    private static final Logger LOGGER = LogManager.getLogger(MD5FileIntegrityCheckerService.class);
    private static MD5FileIntegrityCheckerService instance;

    private MD5FileIntegrityCheckerService() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
    }


    public static MD5FileIntegrityCheckerService getInstance() {
        if (instance == null) {
            try {
                instance = new MD5FileIntegrityCheckerService();
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e);
            }
        }
        return instance;
    }


    @Override
    public String calculateCheckSum(byte[] file) {
        LOGGER.info("Calculating checksum");
        md.update(file);
        String md5 = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        LOGGER.debug("The following checksum is calculated " + md5);
        return md5;
    }

    @Override
    public boolean compareCheckSum(String originCheckSum, String newCheckSum) {
        if (originCheckSum == null || newCheckSum == null) {
            return false;
        }
        return originCheckSum.equals(newCheckSum);
    }

    @Override
    public boolean compareCheckSum(byte[] file, String originCheckSum) {
        String newMD5 = calculateCheckSum(file);
        return compareCheckSum(originCheckSum, newMD5);
    }
}
