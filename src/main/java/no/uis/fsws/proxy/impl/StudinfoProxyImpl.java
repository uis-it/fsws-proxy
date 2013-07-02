/*
 Copyright 2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.fsws.proxy.impl;

import java.util.concurrent.Callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import lombok.Setter;

import no.uis.fsws.proxy.StudinfoProxy;
import no.uis.fsws.studinfo.StudInfoImport;
import no.uis.fsws.studinfo.data.FsStudieinfo;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Implementation using the studinfo-import library. 
 */
@ManagedResource(
  objectName = "uis:service=ws-fsws-proxy,component=studinfo",
  description = "Studinfo Proxy",
  log = false
)
public class StudinfoProxyImpl extends AbstractFswsProxy<StudInfoImport> implements StudinfoProxy {

  @Setter(onMethod = @_(@Required)) private ThreadPoolExecutor executor;
  @Setter private long timeoutMinutes = 30L;
  
  @Override
  public FsStudieinfo getStudieprogramForOrgenhet(final int arstall,
      final String terminkode, final String sprak,
      final int institusjonsnr, final Integer fakultetsnr,
      final Integer instituttnr, final Integer gruppenr,
      final boolean medUPinfo)
  {
    final StudInfoImport svc = getServiceForPrincipal();
    
    try {
      Future<FsStudieinfo> future = executor.submit(new Callable<FsStudieinfo>() {

        @Override
        public FsStudieinfo call() throws Exception {
          return svc.fetchStudyPrograms(institusjonsnr, fakultetsnr != null ? fakultetsnr : -1, arstall, terminkode, medUPinfo, sprak);
        }
      });
      
      return future.get(timeoutMinutes, TimeUnit.MINUTES);
    } catch(ExecutionException | InterruptedException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public FsStudieinfo getStudieprogram(final int arstall, final String terminkode,
      final boolean medUPinfo, final String sprak, final String studieprogramkode)
  {
    final StudInfoImport svc = getServiceForPrincipal();
    try {
      Future<FsStudieinfo> future = executor.submit(new Callable<FsStudieinfo>() {

        @Override
        public FsStudieinfo call() throws Exception {
          return svc.fetchStudyProgram(studieprogramkode, arstall, terminkode, medUPinfo, sprak);
        }
      });
      
      return future.get(timeoutMinutes, TimeUnit.MINUTES);
    } catch(ExecutionException | InterruptedException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param arstall year
   * @param terminkode semester code
   * @param sprak language code
   * @param institusjonsnr FS institution code
   * @param fakultetsnr FS faculty code
   * @param instituttnr ignored
   * @param gruppenr ignored
   */
  @Override
  public FsStudieinfo getEmneForOrgEnhet(final int arstall,
      final String terminkode, final String sprak,
      final int institusjonsnr, final Integer fakultetsnr,
      final Integer instituttnr, final Integer gruppenr)
  {
    final StudInfoImport svc = getServiceForPrincipal();
    try {
      Future<FsStudieinfo> future = executor.submit(new Callable<FsStudieinfo>() {

        @Override
        public FsStudieinfo call() throws Exception {
          return svc.fetchSubjects(institusjonsnr, fakultetsnr, arstall, terminkode, sprak);
        }
      });
      
      return future.get(timeoutMinutes, TimeUnit.MINUTES);
    } catch(ExecutionException | InterruptedException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public FsStudieinfo getEmne(final int arstall, final String terminkode,
      final String sprak, final int institusjonsnr, final String emnekode, final String versjonskode)
  {
    final StudInfoImport svc = getServiceForPrincipal();
    try {
      Future<FsStudieinfo> future = executor.submit(new Callable<FsStudieinfo>() {

        @Override
        public FsStudieinfo call() throws Exception {
          return svc.fetchSubject(institusjonsnr, emnekode, versjonskode, arstall, terminkode, sprak);
        }
      });
      
      return future.get(timeoutMinutes, TimeUnit.MINUTES);
    } catch(ExecutionException | InterruptedException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  @PreDestroy
  public void cleanup() {
    executor.shutdown();
  }
}

