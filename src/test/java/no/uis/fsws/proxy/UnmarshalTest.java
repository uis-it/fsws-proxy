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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import lombok.SneakyThrows;
import no.usit.fsws.schemas.studinfo.Emne;
import no.usit.fsws.schemas.studinfo.FsStudieinfo;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

public class UnmarshalTest {

  @Test
  @SneakyThrows
  public void unmarshalTestUntransformed() {
    final String xml = "<fs-studieinfo xmlns=\"http://fsws.usit.no/schemas/studinfo\">" +
        "  <emne sprak=\"BOKM\u00c5L\">" +
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
        "    <undervisningssemester>V\u00e5r</undervisningssemester>" +
        "    <antall-undsemester>1</antall-undsemester>" +
        "    <undsemester>" +
        "      <semester nr=\"1\">V\u00e5r</semester>" +
        "    </undsemester>" +
        "    <emnetype>VURDERING</emnetype>" +
        "    <periode-eks>" +
        "      <forstegang>2013V</forstegang>" +
        "    </periode-eks>" +
        "    <periode-und>" +
        "      <forstegang>2013V</forstegang>" +
        "    </periode-und>" +
        "    <eksamenssemester>V\u00e5r</eksamenssemester>" +
        "    <inngar-i-studieprogram>" +
        "      <studieprogramkode>B-PETGEO</studieprogramkode>" +
        "      <studieprogramnavn>Petroleumsgeologi - Bachelorstudium i ingeni\u00f8rfag</studieprogramnavn>" +
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
        "        <eksamensdelnavn>Skriftlig 3 timers pr\u00f8ve (60%) og prosjektarbeid basert p\u00e5 feltstudier (40%)</eksamensdelnavn>" +
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
        "        <vurdkombnavn>Skriftlig 3 timers pr\u00f8ve (60%) og prosjektarbeid basert p\u00e5 feltstudier (40%)</vurdkombnavn>" +
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
  
  @Test
  @SneakyThrows
  public void unmarshalTestTransformed() {
    final String xml = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<fs-studieinfo xmlns=\"http://fsws.usit.no/schemas/studinfo\"" +
            "               xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  " +
            "   <emne sprak=\"BOKMÅL\">" +
            "      <emneid>" +
            "         <institusjonsnr>217</institusjonsnr>" +
            "         <emnekode>AG-204</emnekode>" +
            "         <versjonskode>1</versjonskode>" +
            "      </emneid>" +
            "      <emnenavn>The Physical Geography of Svalbard</emnenavn>" +
            "      <emnenavn_en>The Physical Geography of Svalbard</emnenavn_en>" +
            "      <studiepoeng>15</studiepoeng>" +
            "      <status-privatist>N</status-privatist>" +
            "      <studieniva>HN</studieniva>" +
            "      <nuskode>756107</nuskode>" +
            "      <enkeltemneopptak>N</enkeltemneopptak>" +
            "      <studierettkrav>J</studierettkrav>" +
            "      <status_oblig>N</status_oblig>" +
            "      <obligund xsi:nil=\"true\"/>" +
            "      <undervisningssemester>Vår</undervisningssemester>" +
            "      <antall-undsemester>1</antall-undsemester>" +
            "      <antall-forelesningstimer xsi:nil=\"true\"/>" +
            "      <antall-selvstudiumtimer xsi:nil=\"true\"/>" +
            "      <undsemester-liste>" +
            "         <undsemester>" +
            "            <semester-liste>" +
            "               <semester nr=\"1\">Vår</semester>" +
            "            </semester-liste>" +
            "            <forstegang xsi:nil=\"true\"/>" +
            "            <sistegang xsi:nil=\"true\"/>" +
            "         </undsemester>" +
            "      </undsemester-liste>" +
            "      <emnetype>VURDERING</emnetype>" +
            "      <url xsi:nil=\"true\"/>" +
            "      <periode-eks>" +
            "         <forstegang>2013V</forstegang>" +
            "         <sistegang xsi:nil=\"true\"/>" +
            "      </periode-eks>" +
            "      <periode-und>" +
            "         <forstegang>2013V</forstegang>" +
            "         <sistegang xsi:nil=\"true\"/>" +
            "      </periode-und>" +
            "      <eksamenssemester>Vår</eksamenssemester>" +
            "      <anbefalte-forkunnskaper xsi:nil=\"true\"/>" +
            "      <formelle-forkunnskaper xsi:nil=\"true\"/>" +
            "      <inngar-i-studieprogram-liste>" +
            "         <inngar-i-studieprogram>" +
            "            <studieprogramkode>B-PETGEO</studieprogramkode>" +
            "            <studieprogramnavn>Petroleumsgeologi - Bachelorstudium i ingeniørfag</studieprogramnavn>" +
            "         </inngar-i-studieprogram>" +
            "      </inngar-i-studieprogram-liste>" +
            "      <inngar-i-fag-liste>" +
            "         <inngar-i-fag>" +
            "            <fagkode>GEOLOGI</fagkode>" +
            "         </inngar-i-fag>" +
            "      </inngar-i-fag-liste>" +
            "      <fagansvarlig>" +
            "         <institusjonsnr>217</institusjonsnr>" +
            "         <fakultetsnr>8</fakultetsnr>" +
            "         <instituttnr>6</instituttnr>" +
            "         <gruppenr>0</gruppenr>" +
            "         <navn>Institutt for petroleumsteknologi</navn>" +
            "         <avdnavn>Det teknisk-naturvitenskapelige fakultet</avdnavn>" +
            "         <url>http://www.uis.no/fakulteter-institutter-og-sentre/det-teknisk-naturvitenskapelige-fakultet/institutt-for-petroleumsteknologi/</url>" +
            "      </fagansvarlig>" +
            "      <adminansvarlig>" +
            "         <institusjonsnr>217</institusjonsnr>" +
            "         <fakultetsnr>8</fakultetsnr>" +
            "         <instituttnr>6</instituttnr>" +
            "         <gruppenr>0</gruppenr>" +
            "         <navn>Institutt for petroleumsteknologi</navn>" +
            "         <avdnavn>Det teknisk-naturvitenskapelige fakultet</avdnavn>" +
            "         <url>http://www.uis.no/fakulteter-institutter-og-sentre/det-teknisk-naturvitenskapelige-fakultet/institutt-for-petroleumsteknologi/</url>" +
            "      </adminansvarlig>" +
            "      <sprak-liste/>" +
            "      <fagperson-liste/>" +
            "      <dato-eksamen xsi:nil=\"true\"/>" +
            "      <eksamensordning-liste>" +
            "         <eksamensordning>" +
            "            <eksamensordningid>RS</eksamensordningid>" +
            "            <eksamensordningnavn>Rapport(er) og skriftlig(e) eksamen(er)</eksamensordningnavn>" +
            "            <default>J</default>" +
            "            <meld_studentweb>J</meld_studentweb>" +
            "            <hjelpemiddel-liste/>" +
            "            <karregel>A - F</karregel>" +
            "            <dato-eksamen xsi:nil=\"true\"/>" +
            "            <eksamensdel-liste>" +
            "               <eksamensdel>" +
            "                  <eksamensdelnr>0</eksamensdelnr>" +
            "                  <eksamensdelnavn>Skriftlig 3 timers prøve (60%) og prosjektarbeid basert på feltstudier (40%)</eksamensdelnavn>" +
            "                  <studiepoeng-reduksjon xsi:nil=\"true\"/>" +
            "                  <varighet>1 Semestre</varighet>" +
            "                  <eksamensform xsi:nil=\"true\"/>" +
            "                  <karaktervekt xsi:nil=\"true\"/>" +
            "                  <karregel xsi:nil=\"true\"/>" +
            "                  <eksamensdag-liste/>" +
            "                  <dato-eksamen xsi:nil=\"true\"/>" +
            "                  <lovlig-hjelpemiddel-liste/>" +
            "               </eksamensdel>" +
            "            </eksamensdel-liste>" +
            "         </eksamensordning>" +
            "      </eksamensordning-liste>" +
            "      <vurdordning-liste>" +
            "         <vurdordning>" +
            "            <vurdordningid>RS</vurdordningid>" +
            "            <vurdordningnavn>Rapport(er) og skriftlig(e) eksamen(er)</vurdordningnavn>" +
            "            <default>J</default>" +
            "            <meld_studentweb>J</meld_studentweb>" +
            "            <vurdkombinasjon niva=\"1\">" +
            "               <vurdkombkode>SR</vurdkombkode>" +
            "               <vurdkombnavn>Skriftlig 3 timers prøve (60%) og prosjektarbeid basert på feltstudier (40%)</vurdkombnavn>" +
            "               <vurdform xsi:nil=\"true\"/>" +
            "               <vurdkombtype xsi:nil=\"true\"/>" +
            "               <forstegang>2013V</forstegang>" +
            "               <sistegang xsi:nil=\"true\"/>" +
            "               <brok>1/1</brok>" +
            "               <varighet>1 Semestre</varighet>" +
            "               <vurdering>J</vurdering>" +
            "               <alle-kandidater>N</alle-kandidater>" +
            "               <karregelkode>30</karregelkode>" +
            "               <karregel>A - F</karregel>" +
            "               <vurdkombtider>" +
            "                  <vurdkombtid>" +
            "                     <vurdtidkode>06</vurdtidkode>" +
            "                     <vurdtidkode_reell>06</vurdtidkode_reell>" +
            "                     <arsinkrement>0</arsinkrement>" +
            "                     <vurdstatuskode>ORD</vurdstatuskode>" +
            "                     <liketallsaar>J</liketallsaar>" +
            "                     <oddetallsaar>J</oddetallsaar>" +
            "                     <forstegang xsi:nil=\"true\"/>" +
            "                     <sistegang xsi:nil=\"true\"/>" +
            "                  </vurdkombtid>" +
            "               </vurdkombtider>" +
            "               <hjelpemiddel-liste/>" +
            "               <tid xsi:nil=\"true\"/>" +
            "               <tid-reell xsi:nil=\"true\"/>" +
            "               <vurdstatus xsi:nil=\"true\"/>" +
            "               <dato-uttak xsi:nil=\"true\"/>" +
            "               <klokkeslett-uttak xsi:nil=\"true\"/>" +
            "               <dato-innlevering xsi:nil=\"true\"/>" +
            "               <klokkeslett-innlevering xsi:nil=\"true\"/>" +
            "               <dato-eksamen xsi:nil=\"true\"/>" +
            "               <klokkeslett-eksamen-fremmotetid xsi:nil=\"true\"/>" +
            "               <vurdenheter/>" +
            "               <vurdkombinasjon-liste/>" +
            "            </vurdkombinasjon>" +
            "         </vurdordning>" +
            "      </vurdordning-liste>" +
            "      <vektingsreduksjon xsi:nil=\"true\"/>" +
            "      <abs_forkunnskaper_fritekst xsi:nil=\"true\"/>" +
            "      <anb_forkunnskaper_fritekst xsi:nil=\"true\"/>" +
            "      <arbeidsformer xsi:nil=\"true\"/>" +
            "      <innhold><![CDATA[For mer informasjon ang dette emnet vennligst se www.unis.no ]]></innhold>" +
            "      <intro xsi:nil=\"true\"/>" +
            "      <kortsam xsi:nil=\"true\"/>" +
            "      <kvalifik_og_jobbmuligheter xsi:nil=\"true\"/>" +
            "      <litteratur><![CDATA[For mer informasjon ang dette emnet vennligst se www.unis.no ]]></litteratur>" +
            "      <laringsutbytte xsi:nil=\"true\"/>" +
            "      <oblig_undakt_tilleggsinfo xsi:nil=\"true\"/>" +
            "      <opptakskrav xsi:nil=\"true\"/>" +
            "      <praksis xsi:nil=\"true\"/>" +
            "      <studentevaluering xsi:nil=\"true\"/>" +
            "      <vurdering_tilleggstekst xsi:nil=\"true\"/>" +
            "      <apent_for_tillegg xsi:nil=\"true\"/>" +
            "   </emne>" +
            "</fs-studieinfo>";
    

    InputSource unmarshalSource = new InputSource(new StringReader(xml));
    JAXBContext jc = JAXBContext.newInstance("no.usit.fsws.schemas.studinfo", FsStudieinfo.class.getClassLoader());
    Unmarshaller u = jc.createUnmarshaller();
    Schema schema = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(new File("src/main/wsdl/studinfo.xsd"));
    u.setSchema(schema);
    FsStudieinfo sinfo = (FsStudieinfo)u.unmarshal(unmarshalSource);

    Assert.assertThat(sinfo, is(notNullValue()));
    Assert.assertThat(sinfo.getEmne(), hasItem(notNullValue(Emne.class)));
  }
}
