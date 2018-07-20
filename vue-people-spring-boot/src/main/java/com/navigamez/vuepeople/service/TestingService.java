package com.navigamez.vuepeople.service;

import com.navigamez.greex.GreexGenerator;
import com.navigamez.vuepeople.model.ContactInfo;
import com.navigamez.vuepeople.model.ContactInfoType;
import com.navigamez.vuepeople.model.Person;
import com.navigamez.vuepeople.repository.PersonRepository;
import com.navigamez.vuepeople.util.RandomManager;
import com.navigamez.vuepeople.util.SimpleDateFormatManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestingService {

    private static final Logger logger = LoggerFactory.getLogger(TestingService.class);

    private final PersonRepository personRepository;

    private final List<ContactInfoType> usernameInfoTypes = Arrays.asList(
            ContactInfoType.TWITTER,
            ContactInfoType.FACEBOOK,
            ContactInfoType.STACKOVERFLOW,
            ContactInfoType.GITHUB,
            ContactInfoType.BITBUCKET
    );

    private final String PHONE_REGEX = "[2-7][0-9]{2}555[0-9]{4}";
    private final GreexGenerator phoneNumberGenerator = new GreexGenerator(PHONE_REGEX);

    private final String CAPITAL_VOWELS = "[AEIOU]";
    private final String CAPITAL_CONSONANTS = "[BCDFGHJKLMNPQRSTVWXYZ]";
    private final String LOWER_VOWELS = "[aeiou]";
    private final String LOWER_CONSONANTS = "[bcdfghjklmnpqrstvwxyz]";
    private final String NAME_PART_1 = CAPITAL_VOWELS + "(" + LOWER_CONSONANTS + "{1,2}" + LOWER_VOWELS + "{1,2}" + "){1,3}(" + LOWER_CONSONANTS + ")?";
    private final String NAME_PART_2 = CAPITAL_CONSONANTS + "(" + LOWER_VOWELS + "{1,2}" + LOWER_CONSONANTS + "{1,2}" + "){1,3}(" + LOWER_VOWELS + ")?";
    private final String NAME_REGEX = "(" + NAME_PART_1 + "|" + NAME_PART_2 + ")";
    private final GreexGenerator nameGenerator = new GreexGenerator(NAME_REGEX);

    public TestingService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void deleteAll() {
        personRepository.deleteAll();
    }

    public void fill(int count) {
        if (count > 10000) {
            throw new IllegalArgumentException("Count cannot be over 10,000.");
        }
        long totalExisting = personRepository.count();
        if (totalExisting >= count) {
            return;
        }
        List<Person> persons = new ArrayList<>((int) (count - totalExisting));
        for (long i = totalExisting; i < count; i++) {
            if (i % 10 == 0) {
                logger.info("Creating record " + (i + 1) + " of " + (count - totalExisting));
            }
            Random random = RandomManager.get();
            Person person = new Person();
            person.setGivenName(nameGenerator.generateRandom(random));
            person.setFamilyName(nameGenerator.generateRandom(random));
            person.setBirthDate(randomBirthDate(random));
            List<ContactInfoType> contactInfoTypes = new ArrayList<>(Arrays.asList(ContactInfoType.values()));
            int infoCount = random.nextInt(contactInfoTypes.size()) + 1;
            for (int j = 0; j < infoCount; j++) {
                ContactInfoType type = contactInfoTypes.remove(random.nextInt(contactInfoTypes.size()));
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setType(type);
                contactInfo.setData(generateValue(type, person, random));
                if (type == ContactInfoType.CUSTOM) {
                    contactInfo.setCustomLabel(nameGenerator.generateRandom(random));
                }
                person.getContactInfos().add(contactInfo);
            }
            persons.add(person);
        }
        personRepository.saveAll(persons);
    }

    private String generateValue(ContactInfoType type, Person person, Random random) {
        switch (type) {
            case MOBILE:
            case HOME:
            case WORK:
            case FAX:
                return phoneNumberGenerator.generateRandom(random);
            case TWITTER:
            case FACEBOOK:
            case STACKOVERFLOW:
            case GITHUB:
            case BITBUCKET:
                return generateUsername(person, random);
            case EMAIL:
                return generateUsername(person, random) + "@example.com";
            case CUSTOM:
            default:
                if (random.nextInt() < 0.7) {
                    return generateUsername(person, random);
                }
                if (random.nextInt() < 0.7) {
                    return phoneNumberGenerator.generateRandom(random);
                }
                return nameGenerator.generateRandom(random);
        }
    }

    private String generateUsername(Person person, Random random) {
        List<ContactInfo> usernameInfos = person.getContactInfos().stream()
                .filter(contactInfo -> usernameInfoTypes.contains(contactInfo.getType()))
                .collect(Collectors.toList());
        if (!usernameInfos.isEmpty() && random.nextDouble() < 0.6) {
            ContactInfo contactInfo = usernameInfos.get(random.nextInt(usernameInfos.size()));
            return contactInfo.getData();
        }
        String givenName = person.getGivenName().toLowerCase();
        String familyName = person.getFamilyName().toLowerCase();
        String birthYear2 = person.getBirthDate().substring(2, 2);
        String birthYear4 = person.getBirthDate().substring(0, 4);
        StringBuilder sb = new StringBuilder();
        if (random.nextDouble() < 0.2) {
            sb.append(givenName);
            if (random.nextDouble() < 0.9) {
                sb.append('_').append(familyName);
            }
        } else {
            sb.append(givenName, 0, 1);
            if (random.nextDouble() < 0.05) {
                sb.append('_');
            }
            sb.append(familyName);
        }
        if (random.nextDouble() < 0.3) {
            if (random.nextDouble() < 0.05) {
                sb.append("_");
            }
            sb.append(random.nextBoolean() ? birthYear2 : birthYear4);
        }
        return sb.toString();
    }

    private String randomBirthDate(Random random) {
        int daysBack = random.nextInt(70 * 365) + 20 * 365;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysBack);
        return SimpleDateFormatManager.get("yyyy-MM-dd").format(cal.getTime());
    }
}
