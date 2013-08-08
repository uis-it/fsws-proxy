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

import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;

import lombok.Cleanup;
import lombok.SneakyThrows;

import no.uis.fsws.studinfo.impl.SkippingAmpersandParser;
import no.uis.fsws.studinfo.impl.StudInfoImportImpl;
import no.usit.fsws.schemas.studinfo.Emne;
import no.usit.fsws.schemas.studinfo.FsStudieinfo;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class UnmarshalTest {

  @Test
  @SneakyThrows
  public void unmarshalTest() {
    final String xml = "<fs-studieinfo xmlns=\"http://fsws.usit.no/schemas/studinfo\">" +
        "  <emne sprak=\"BOKMÅL\">" +
        "    <emneid>" +
        "      <institusjonsnr>217</institusjonsnr>" +
        "      <emnekode>AG-204</emnekode>" +
        "      <versjonskode>1</versjonskode>" +
        "    </emneid>" +
        "    <emnenavn>The Physical Geography of Svalbard</emnenavn>" +
        "    <emnenavn_en>The Physical Geography of Svalbard</emnenavn_en>" +
        "    <studiepoeng>15</studiepoeng>" +
        "    <status-privatist>N</status-privatist>" +
        "    <studieniva>HN</studieniva>" +
        "    <nuskode>756107</nuskode>" +
        "    <enkeltemneopptak>N</enkeltemneopptak>" +
        "    <studierettkrav>J</studierettkrav>" +
        "    <status_oblig>N</status_oblig>" +
        "    <undervisningssemester>Vår</undervisningssemester>" +
        "    <antall-undsemester>1</antall-undsemester>" +
        "    <undsemester>" +
        "      <semester nr=\"1\">Vår</semester>" +
        "    </undsemester>" +
        "    <emnetype>VURDERING</emnetype>" +
        "    <periode-eks>" +
        "      <forstegang>2013V</forstegang>" +
        "    </periode-eks>" +
        "    <periode-und>" +
        "      <forstegang>2013V</forstegang>" +
        "    </periode-und>" +
        "    <eksamenssemester>Vår</eksamenssemester>" +
        "    <inngar-i-studieprogram>" +
        "      <studieprogramkode>B-PETGEO</studieprogramkode>" +
        "      <studieprogramnavn>Petroleumsgeologi - Bachelorstudium i ingeniørfag</studieprogramnavn>" +
        "    </inngar-i-studieprogram>" +
        "    <inngar-i-fag>" +
        "      <fagkode>GEOLOGI</fagkode>" +
        "    </inngar-i-fag>" +
        "    <sted type=\"fagansvarlig\">" +
        "      <institusjonsnr>217</institusjonsnr>" +
        "      <fakultetsnr>8</fakultetsnr>" +
        "      <instituttnr>6</instituttnr>" +
        "      <gruppenr>0</gruppenr>" +
        "      <navn>Institutt for petroleumsteknologi</navn>" +
        "      <avdnavn>Det teknisk-naturvitenskapelige fakultet</avdnavn>" +
        "      <url>http://www.uis.no/fakulteter-institutter-og-sentre/det-teknisk-naturvitenskapelige-fakultet/institutt-for-petroleumsteknologi/</url>" +
        "    </sted>" +
        "    <sted type=\"adminansvarlig\">" +
        "      <institusjonsnr>217</institusjonsnr>" +
        "      <fakultetsnr>8</fakultetsnr>" +
        "      <instituttnr>6</instituttnr>" +
        "      <gruppenr>0</gruppenr>" +
        "      <navn>Institutt for petroleumsteknologi</navn>" +
        "      <avdnavn>Det teknisk-naturvitenskapelige fakultet</avdnavn>" +
        "      <url>http://www.uis.no/fakulteter-institutter-og-sentre/det-teknisk-naturvitenskapelige-fakultet/institutt-for-petroleumsteknologi/</url>" +
        "    </sted>" +
        "    <eksamensordning>" +
        "      <eksamensordningid>RS</eksamensordningid>" +
        "      <eksamensordningnavn>Rapport(er) og skriftlig(e) eksamen(er)</eksamensordningnavn>" +
        "      <default>J</default>" +
        "      <meld_studentweb>J</meld_studentweb>" +
        "      <karregel>A - F</karregel>" +
        "      <eksamensdel>" +
        "        <eksamensdelnr>0</eksamensdelnr>" +
        "        <eksamensdelnavn>Skriftlig 3 timers prøve (60%) og prosjektarbeid basert på feltstudier (40%)</eksamensdelnavn>" +
        "        <varighet>1 Semestre</varighet>" +
        "        <dato-eksamen></dato-eksamen>" +
        "      </eksamensdel>" +
        "    </eksamensordning>" +
        "    <vurdordning>" +
        "      <vurdordningid>RS</vurdordningid>" +
        "      <vurdordningnavn>Rapport(er) og skriftlig(e) eksamen(er)</vurdordningnavn>" +
        "      <default>J</default>" +
        "      <meld_studentweb>J</meld_studentweb>" +
        "      <vurdkombinasjon niva=\"1\">" +
        "        <vurdkombkode>SR</vurdkombkode>" +
        "        <vurdkombnavn>Skriftlig 3 timers prøve (60%) og prosjektarbeid basert på feltstudier (40%)</vurdkombnavn>" +
        "        <forstegang>2013V</forstegang>" +
        "        <varighet>1 Semestre</varighet>" +
        "        <vurdering>J</vurdering>" +
        "        <karregelkode>30</karregelkode>" +
        "        <karregel>A - F</karregel>" +
        "        <vurdkombtider>" +
        "          <vurdkombtid>" +
        "            <vurdtidkode>06</vurdtidkode>" +
        "            <vurdtidkode_reell>06</vurdtidkode_reell>" +
        "            <arsinkrement>0</arsinkrement>" +
        "            <vurdstatuskode>ORD</vurdstatuskode>" +
        "            <liketallsaar>J</liketallsaar>" +
        "            <oddetallsaar>J</oddetallsaar>" +
        "            <forstegang></forstegang>" +
        "          </vurdkombtid>" +
        "        </vurdkombtider>" +
        "      </vurdkombinasjon>" +
        "    </vurdordning>" +
        "    <innhold>For mer informasjon ang dette emnet vennligst se www.unis.no </innhold>" +
        "    <litteratur>For mer informasjon ang dette emnet vennligst se www.unis.no </litteratur>" +
        "  </emne>" +
        "</fs-studieinfo>";
    
    
    StudInfoImportImpl impl = new StudInfoImportImpl() {
      @Override
      protected Reader fsGetEmne(int institution, int faculty, int year, String semester, String language) {
        return new StringReader(xml);
      }
    };
    
    impl.setTransformerUrl(Thread.currentThread().getContextClassLoader().getResource("fspreprocess-uis.xsl"));
    impl.setXmlSourceParser(SkippingAmpersandParser.class.getName());
    FsStudieinfo sinfo = impl.fetchSubjects(0, 0, 0, null, null);
    Assert.assertThat(sinfo, is(notNullValue()));
    Assert.assertThat(sinfo.getEmne(), hasItem(notNullValue(Emne.class)));
  }
}
