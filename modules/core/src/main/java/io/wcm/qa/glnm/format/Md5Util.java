/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.qa.glnm.format;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import io.wcm.qa.glnm.exceptions.GaleniumException;

final class Md5Util {

  private Md5Util() {
    // do not instantiate
  }

  public static String getMd5AsAscii(byte[] input) {
    try {
      MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
      byte[] digestedBytes = digest.digest(input);
      return getBytesAsAscii(digestedBytes);
    }
    catch (NoSuchAlgorithmException ex) {
      throw new GaleniumException("Java not remembering MD5.", ex);
    }
  }

  private static String getBytesAsAscii(byte[] bytesToEncode) {
    byte[] encodedBytes = Base64.getEncoder().encode(bytesToEncode);
    return new String(encodedBytes, StandardCharsets.US_ASCII);
  }

}
