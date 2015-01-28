package org.esn.mobilit.utils;

/**
 * Created by Spider on 26/01/15.
 */
public interface ApplicationConstants {

    // Php Application URL to store Reg ID created
    static final String APP_SERVER_URL = "http://ixesn.fr/webservices/gcm/gcm.php?shareRegId=true";

    // Google Project Number
    static final String GOOGLE_PROJ_ID = "1079816997";

    // Message Key
    static final String MSG_KEY = "m";

    // Path for news event & partners
    static final String NEWS_PATH = "/news";
    static final String PARTNERS_PATH = "/partners";
    static final String EVENTS_PATH = "/events";

    // Path for feed
    static final String FEED_PATH = "/feed";

    // Webservices
    // Survival Guide Webservice
    static final String SURVIVAL_WEBSERVICE_URL = "http://esnlille.fr/survivalGuide/json/";

    //Sections webservice
    static final String SECTIONS_WEBSERVICE_URL = "http://www.esnlille.fr/WebServices/Sections/";

    //Survival Guide JSON
    static final String jsonboitenoir = "{\n" +
            "  \"categories\": [\n" +
            "    {\n" +
            "      \"id\": \"2\",\n" +
            "      \"name\": \"L'université\",\n" +
            "      \"section\": \"FR-LILLE-ESL\",\n" +
            "      \"content\": \"\",\n" +
            "      \"position\": \"0\",\n" +
            "      \"categories\": [\n" +
            "        {\n" +
            "          \"id\": \"10\",\n" +
            "          \"name\": \"L'Université Lille 1\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"L'université Lille 1 est une Université publique située à Villeneuve d'Ascq (10 km de Lille) sur un vaste campus de 110 hectares. Elle compte près de 20000 étudiants dont 20 % d'étudiants internationaux.\\n\\nL'université Lille 1 propose des diplômes de Licence, de Master et de Doctorat dans les secteurs suivants : sciences exactes, sciences de l'ingénieur et sciences économiques et sociales.\\n\\nL'internationalisation des formations est au coeur du projet l'établissement. Ainsi, plus de 1000 étudiants effectuent chaque année une mobilité à l'international.\\n\\nLille 1 est par ailleurs la troisième Université en France la plus active dans le cadre du programme Erasmus. Afin de mettre en oeuvre sa politique, Lille 1 signe des accords de partenariat avec des établissements d'enseignement supérieur à l'étranger et participe à de nombreux programmes européens.\\n\\nElle développe aussi des formations totalement enseignées\\nen anglais et des doubles-diplômes avec des Universités partenaires.\\n\\n Afin de valoriser le parcours international de ses étudiants,  l'Université Lille 1 a mis en place un Label international qui est décerné à tout étudiant ayant effectué une mobilité à l\u0092étranger, ayant une certification en langue étrangère de niveau B2 et ayant suivi un module de communication interculturelle.\\nCe Label a été distingué au niveau national en 2012.\\n\\nL'\u0092Université se distingue aussi au niveau de la recherche car elle\\ndispose d'équipements de haute technicité qui attirent de nombreux doctorants internationaux (plus de 40 % des doctorants inscrits à Lille 1 sont de nationalité étrangère).\",\n" +
            "          \"position\": \"0\",\n" +
            "          \"categories\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"9\",\n" +
            "          \"name\": \"International Student Week\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"L'Université de Lille va organiser à la fin du mois de novembre (du 24 au 28 novembre) la deuxième édition de la Semaine Internationale destinée à tous les étudiants de trois campus (Lille1, Lille2 et Lille3).\\n\\nCette Semaine sera l'occasion d'organiser des événements festifs autour de l'international : conférences, cafés-langues, théâtre, rencontres, soirées, débats, etc. \\n\\nMerci de noter, d'ores et déjà, les dates sur votre agenda et si vous souhaitez organiser un événement, n\u0092hésitez pas à vous rapprocher de votre Service des Relations internationales !\",\n" +
            "          \"position\": \"0\",\n" +
            "          \"categories\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"3\",\n" +
            "      \"name\": \"Ton arrivée\",\n" +
            "      \"section\": \"FR-LILLE-ESL\",\n" +
            "      \"content\": \"\",\n" +
            "      \"position\": \"0\",\n" +
            "      \"categories\": [\n" +
            "        {\n" +
            "          \"id\": \"11\",\n" +
            "          \"name\": \"En atterrissant a Bruxelles - Charleroi\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"La société de transport Flibco met à disposition une navette entre la gare Lille Europe et l\u0092aéroport de Charleroi.\\n\\nLes prix sont très intéressants, tu peux trouver un ticket de 5 à 16\u0080. Tu peux réserver ton billet sur : www.flibco.com\",\n" +
            "          \"position\": \"0\",\n" +
            "          \"categories\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"12\",\n" +
            "          \"name\": \"La mutuelle étudiante\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"La sécurité étudiante te garantit un remboursement\\npartiel de tes frais de santé mais elle ne prend en charge en moyenne que 65% de tes dépenses.\\nPour éviter les dépenses de santé imprévues, tu peux choisir une mutuelle complémentaire qui améliorera tes remboursements.\",\n" +
            "          \"position\": \"0\",\n" +
            "          \"categories\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"name\": \"ESN Lille\",\n" +
            "      \"section\": \"FR-LILLE-ESL\",\n" +
            "      \"content\": \"\",\n" +
            "      \"position\": \"2\",\n" +
            "      \"categories\": [\n" +
            "        {\n" +
            "          \"id\": \"8\",\n" +
            "          \"name\": \"Le bureau ESN Lille\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"Si tu souhaites nous rencontrer, pour t'informer ou même discuter, tu peux nous retrouver dans notre bureau qui se trouve à  l'étage de la Maison Des Etudiants à  l'arrêt de métro Cité Scientifique (ligne 1).\",\n" +
            "          \"position\": \"5\",\n" +
            "          \"categories\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"4\",\n" +
            "      \"name\": \"Lille et sa région\",\n" +
            "      \"section\": \"FR-LILLE-ESL\",\n" +
            "      \"content\": \"\",\n" +
            "      \"position\": \"3\",\n" +
            "      \"categories\": [\n" +
            "        {\n" +
            "          \"id\": \"13\",\n" +
            "          \"name\": \"La ville de Lille\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"Lille est une terre d\u0092échanges, de croisements et de migrations. Aujourd\u0092hui, Lille, grâce à ses habitants, est devenue un véritable point de rencontre des cultures nord européennes et méditerranéennes, un territoire d\u0092accueil et de métissages. Son histoire riche et contrastée a doté sa population d\u0092une incroyable force de caractère.\",\n" +
            "          \"position\": \"0\",\n" +
            "          \"categories\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"5\",\n" +
            "      \"name\": \"Infos Pratiques\",\n" +
            "      \"section\": \"FR-LILLE-ESL\",\n" +
            "      \"content\": \"\",\n" +
            "      \"position\": \"4\",\n" +
            "      \"categories\": [\n" +
            "        {\n" +
            "          \"id\": \"6\",\n" +
            "          \"name\": \"Se déplacer en France\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"\",\n" +
            "          \"position\": \"-5\",\n" +
            "          \"categories\": [\n" +
            "            {\n" +
            "              \"id\": \"14\",\n" +
            "              \"name\": \"En train\",\n" +
            "              \"section\": \"FR-LILLE-ESL\",\n" +
            "              \"content\": \"Les trains en France sont gérés par la SNCF. Le train reste un bon moyen pour se déplacer en France et dans la région à coût raisonnable. \\nDe plus, si tu as moins de 28 ans, tu peux faire une carte 'Jeune' pour 50 euros qui te permet d'avoir des réductions sur tes billets. Tu trouveras plus d'informations sur le site de la SNCF : www.voyages-sncf.com. \\n\\nD'autres services low costs intéressants sont proposés par la SNCF:\\niDBus (fr.idbus.com) et OUIGO (www.ouigo.com).\",\n" +
            "              \"position\": \"0\",\n" +
            "              \"categories\": []\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": \"15\",\n" +
            "              \"name\": \"En covoiturage\",\n" +
            "              \"section\": \"FR-LILLE-ESL\",\n" +
            "              \"content\": \"Tu peux aussi te déplacer en France avec le système de covoiturage.\\nPlusieurs sites te permettent de réserver une place d\u0092une voiture que quelqu'un met à disposition pour son trajet. Les prix ne sont pas très\\nélevés et ça te permet d\u0092avoir quelqu\u0092un avec qui discuter pendant le trajet.\\nPlus d\u0092informations sur :\\nhttp://www.covoiturage.fr\",\n" +
            "              \"position\": \"0\",\n" +
            "              \"categories\": []\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"16\",\n" +
            "          \"name\": \"Se déplacer en Belgique\",\n" +
            "          \"section\": \"FR-LILLE-ESL\",\n" +
            "          \"content\": \"Pour voyager en Belgique à moindre prix, tu peux utiliser le train, géré par la SNCB (Compagnie de train belge). Si tu as moins de 26 ans , le plus intéressant pour toi sera sans doute le Go Pass 10. Ce pass qui coute 50 euros permet de faire 10 trajets partout en Belgique. De plus, ce pass n'est pas réservé à une personne. Tu peux donc le partager avec tes amis.\\n\\nTu pourras également le donner ou le revendre si tu ne comptes plus l'utiliser. Ce ticket ne marche qu'en Belgique, donc la meilleure solution est de te rendre à la gare de Mouscron ou de Tournai grâce à un billet SNCF et ensuite de te rendre à la destination de ton choix avec ton Go Pass.\\n\\nTu pourras l'acheter dans les distributeurs automatiques ou guichets des gares belges. Tu trouveras plus d\u0092infos sur le site de la SNCB, voici le lien: www.belgianrail.be\",\n" +
            "          \"position\": \"0\",\n" +
            "          \"categories\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"44\",\n" +
            "      \"name\": \"test\",\n" +
            "      \"section\": \"FR-LILLE-ESL\",\n" +
            "      \"content\": \"blabla\",\n" +
            "      \"position\": \"56\",\n" +
            "      \"categories\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"51\",\n" +
            "      \"name\": \"test\",\n" +
            "      \"section\": \"FR-LILLE-ESL\",\n" +
            "      \"content\": \"test blabla\",\n" +
            "      \"position\": \"92\",\n" +
            "      \"categories\": []\n" +
            "    }\n" +
            "  ]\n" +
            "}";

}
