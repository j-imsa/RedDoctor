package be.jimsa.reddoctor.utility.id;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;


@Component
public class PublicIdGenerator {

    public String generatePublicId(int length) {
        if (length < PUBLIC_ID_MIN_LENGTH || length > PUBLIC_ID_MAX_LENGTH) {
            length = PUBLIC_ID_DEFAULT_LENGTH;
        }
        return IntStream.range(0, length)
                .map(i -> PUBLIC_ID_RANDOM.nextInt(PUBLIC_ID_CHARACTERS.length()))
                .mapToObj(PUBLIC_ID_CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
