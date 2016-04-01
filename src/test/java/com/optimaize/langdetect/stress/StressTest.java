package com.optimaize.langdetect.stress;

import com.google.common.io.Files;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

@Ignore // Takes about one hour to finish.
public class StressTest {

    private static final int REPETITIONS = 1000000;

    @Test
    public void stressTest() throws Exception {
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
        LanguageDetector detector = LanguageDetectorBuilder.create(NgramExtractors.standard()).withProfiles(languageProfiles).build();
        URL resource = StressTest.class.getResource("/texts/de-wikipedia-Deutschland.txt");
        if (resource == null) {
            Assert.fail("expected a resource file");
        }
        String file = resource.getFile();
        String text = Files.toString(new File(file), Charset.forName("UTF-8"));
        for (int i = 0; i < REPETITIONS; i++) {
            TextObject textObject = textObjectFactory.forText(text);
            try {
                Assert.assertEquals(LdLocale.fromString("de"), detector.detect(textObject).orNull());
            } catch (Throwable throwable) {
                System.err.println(throwable.getMessage());
            }
        }
    }

}
