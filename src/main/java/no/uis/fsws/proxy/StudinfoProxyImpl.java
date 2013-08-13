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

package no.uis.fsws.proxy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Setter;
import lombok.SneakyThrows;
import no.usit.fsws.schemas.studinfo.Emne;
import no.usit.fsws.schemas.studinfo.FsStudieinfo;
import no.usit.fsws.schemas.studinfo.Kurs;
import no.usit.fsws.schemas.studinfo.Sprakkode;
import no.usit.fsws.schemas.studinfo.Studieprogram;
import no.usit.fsws.schemas.studinfo.StudinfoProxy;
import no.usit.fsws.schemas.studinfo.Terminkode;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Implementation using the studinfo-import library.
 */
@ManagedResource(objectName = "uis:service=ws-fsws-proxy,component=studinfo", description = "Studinfo Proxy", log = false)
public class StudinfoProxyImpl extends AbstractFswsProxy<StudInfoImport> implements StudinfoProxy {

  @Setter(onMethod = @_(@Required)) private ThreadPoolExecutor executor;
  @Setter private long timeoutMinutes = 30L;

  @Override
  public List<Studieprogram> getStudieprogrammerForOrgenhet(final XMLGregorianCalendar arstall, final Terminkode terminkode,
      final Sprakkode sprak, final int institusjonsnr, final Integer fakultetsnr, final Integer instituttnr,
      final Integer gruppenr, final boolean medUPinfo)
  {
    final StudInfoImport svc = getServiceForPrincipal();

    try {
      Future<List<Studieprogram>> future = executor.submit(new Callable<List<Studieprogram>>() {

        @Override
        public List<Studieprogram> call() throws Exception {
          final FsStudieinfo sinfo = svc.fetchStudyPrograms(institusjonsnr, fakultetsnr != null ? fakultetsnr : -1,
            arstall.getYear(), terminkode.toString(), medUPinfo, sprak.toString());
          return sinfo.getStudieprogram();
        }
      });

      return future.get(timeoutMinutes, TimeUnit.MINUTES);
    } catch(ExecutionException | InterruptedException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Studieprogram> getStudieprogram(final XMLGregorianCalendar arstall, final Terminkode terminkode,
      final Sprakkode sprak, final boolean medUPinfo, final String studieprogramkode)
  {
    final StudInfoImport svc = getServiceForPrincipal();
    try {
      Future<List<Studieprogram>> future = executor.submit(new Callable<List<Studieprogram>>() {

        @Override
        public List<Studieprogram> call() throws Exception {
          final FsStudieinfo sinfo = svc.fetchStudyProgram(studieprogramkode, arstall.getYear(), terminkode.toString(),
            medUPinfo, sprak.toString());
          return sinfo.getStudieprogram();
        }
      });

      return future.get(timeoutMinutes, TimeUnit.MINUTES);
    } catch(ExecutionException | InterruptedException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @SneakyThrows
  public List<Emne> getEmnerForOrgenhet(final XMLGregorianCalendar arstall, final Terminkode terminkode, final Sprakkode sprak,
      final int institusjonsnr, final Integer fakultetsnr, final Integer instituttnr, final Integer gruppenr)
  {
    final StudInfoImport svc = getServiceForPrincipal();
    Future<List<Emne>> future = executor.submit(new Callable<List<Emne>>() {

      @Override
      public List<Emne> call() throws Exception {
        final FsStudieinfo sinfo = svc.fetchSubjects(institusjonsnr, fakultetsnr == null ? -1 : fakultetsnr.intValue(),
          arstall.getYear(), terminkode.toString(), sprak.toString());
        return sinfo.getEmne();
      }
    });

    return future.get(timeoutMinutes, TimeUnit.MINUTES);
  }

  @Override
  @SneakyThrows
  public List<Emne> getEmne(final XMLGregorianCalendar arstall, final Terminkode terminkode, final Sprakkode sprak,
      final int institusjonsnr, final String emnekode, final String versjonskode)
  {
    final StudInfoImport svc = getServiceForPrincipal();
    Future<List<Emne>> future = executor.submit(new Callable<List<Emne>>() {

      @Override
      public List<Emne> call() throws Exception {
        final FsStudieinfo sinfo = svc.fetchSubject(institusjonsnr, emnekode, versjonskode, arstall.getYear(),
          terminkode.toString(), sprak.toString());
        return sinfo.getEmne();
      }
    });

    return future.get(timeoutMinutes, TimeUnit.MINUTES);
  }

  @Override
  @SneakyThrows
  public List<Kurs> getKurs(final XMLGregorianCalendar arstall, final Terminkode terminkode, final Sprakkode sprak, final int institusjonsnr,
    Integer fakultetsnr, Integer instituttnr, Integer gruppenr)
    {
    final StudInfoImport svc = getServiceForPrincipal();
    
    Future<List<Kurs>> future = executor.submit(new Callable<List<Kurs>>() {

      @Override
      public List<Kurs> call() throws Exception {
        final FsStudieinfo sinfo = svc.fetchCourses(institusjonsnr, arstall.getYear(), terminkode.toString(), sprak.toString());
        return sinfo.getKurs();
      }
    });
    
    return future.get(timeoutMinutes, TimeUnit.MINUTES);
  }
  
  @PreDestroy
  public void cleanup() {
    executor.shutdown();
  }

}
