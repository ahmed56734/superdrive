package com.ahmeds.superdrive.services;

import com.ahmeds.superdrive.mapper.CredentialMapper;
import com.ahmeds.superdrive.models.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialsByUser(Integer userId) {
        List<Credential> credentials = credentialMapper.getCredentialsByUser(userId);
        credentials.forEach(credential -> {
            String decryptedPassword = encryptionService.decryptValue(
                    credential.getPassword(), credential.getKey());
            credential.setPassword(decryptedPassword);
        });
        return credentials;
    }

    public Credential getCredentialById(Integer credentialId) {
        Credential credential = credentialMapper.getCredentialById(credentialId);
        if (credential != null) {
            String decryptedPassword = encryptionService.decryptValue(
                    credential.getPassword(), credential.getKey());
            credential.setPassword(decryptedPassword);
        }
        return credential;
    }

    public void addCredential(Credential credential) {
        String encodedKey = generateEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        credentialMapper.insert(credential);
    }

    public void updateCredential(Credential credential) {
        String encodedKey = generateEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        credentialMapper.update(credential);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    private String generateEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
