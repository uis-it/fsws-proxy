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

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import no.uis.fsws.studinfo.data.FsStudieinfo;
/**
 * Proxy for FS-WS Studinfo.
 * @see https://www.fellesstudentsystem.no/dokumentasjon/teknisk/fsws-dok/studinfo/index.html
 * 
 */
@WebService(targetNamespace = "http://fsws.usit.no/schemas/studinfo")
public interface StudinfoProxy {
  //  CHECKSTYLE:OFF

  FsStudieinfo getStudieprogramForOrgenhet(
      @WebParam(name="arstall") int arstall,
      @WebParam(name="terminkode") @XmlElement(required=true) String terminkode,
      @WebParam(name="sprak") @XmlElement(required=true) String sprak,
      @WebParam(name="institusjonsnr") int institusjonsnr, 
      @WebParam(name="fakultetsnr") Integer fakultetsnr, 
      @WebParam(name="instituttnr") Integer instituttnr, 
      @WebParam(name="gruppenr") Integer gruppenr,
      @WebParam(name="medUPinfo") boolean medUPinfo
      );
  
  FsStudieinfo getStudieprogram(
      @WebParam(name="arstall") int arstall,
      @WebParam(name="terminkode") @XmlElement(required=true) String terminkode,
      @WebParam(name="medUPinfo") boolean medUPinfo,
      @WebParam(name="sprak") @XmlElement(required=true) String sprak,
      @WebParam(name="studieprogramkode") @XmlElement(required=true) String studieprogramkode
      );
  
  FsStudieinfo getEmneForOrgEnhet(
      @WebParam(name="arstall") int arstall,
      @WebParam(name="terminkode") @XmlElement(required=true) String terminkode,
      @WebParam(name="sprak") @XmlElement(required=true) String sprak,
      @WebParam(name="institusjonsnr") int institusjonsnr, 
      @WebParam(name="fakultetsnr") Integer fakultetsnr, 
      @WebParam(name="instituttnr") Integer instituttnr, 
      @WebParam(name="gruppenr") Integer gruppenr
      );
  
  FsStudieinfo getEmne(
      @WebParam(name="arstall") int arstall,
      @WebParam(name="terminkode") @XmlElement(required=true) String terminkode,
      @WebParam(name="sprak") @XmlElement(required=true) String sprak,
      @WebParam(name="institusjonsnr") int institusjonsnr,
      @WebParam(name="emnekode") @XmlElement(required=true) String emnekode,
      @WebParam(name="versjonskode") @XmlElement(required=true) String versjonskode
      );
}

