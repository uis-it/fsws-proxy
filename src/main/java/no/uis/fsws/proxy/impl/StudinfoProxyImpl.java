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

import no.uis.fsws.proxy.StudinfoProxy;
import no.uis.fsws.studinfo.StudInfoImport;
import no.uis.fsws.studinfo.data.FsStudieinfo;

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

  @Override
  public FsStudieinfo getStudieprogramForOrgenhet(int arstall,
      String terminkode, String sprak,
      int institusjonsnr, Integer fakultetsnr,
      Integer instituttnr, Integer gruppenr,
      boolean medUPinfo)
  {
    StudInfoImport svc = getServiceForPrincipal();
    
    if (svc != null) {
      try {
        return svc.fetchStudyPrograms(institusjonsnr, fakultetsnr != null ? fakultetsnr : -1, arstall, terminkode, medUPinfo, sprak);
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  @Override
  public FsStudieinfo getStudieprogram(int arstall, String terminkode,
      boolean medUPinfo, String sprak,
      String studieprogramkode)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FsStudieinfo getEmneForOrgEnhet(int arstall,
      String terminkode, String sprak,
      int institusjonsnr, Integer fakultetsnr,
      Integer instituttnr, Integer gruppenr)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FsStudieinfo getEmne(int arstall, String terminkode,
      boolean medUPinfo, String sprak,
      String emnekode, String versjonskode)
  {
    // TODO Auto-generated method stub
    return null;
  }

}

