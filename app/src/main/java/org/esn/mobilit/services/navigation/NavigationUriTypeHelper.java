package org.esn.mobilit.services.navigation;

public class NavigationUriTypeHelper {

    public NavigationUriType parseNavigationUriType(String uri) throws UnknownNavigationUriException {
        if (!uri.contains("/")) {
            // we have an "easy" uri without parameters
            for (NavigationUriType navigationUriType : NavigationUriType.getAllNavigationsUriTypesWithoutParameters()) {
                if (navigationUriType.getType().equals(uri)) {
                    return navigationUriType;
                }
            }
        } else {
            String[] uriToFindSplit = uri.split("/");
            for (NavigationUriType navigationUriType : NavigationUriType.getAllNavigationsUriTypesWithParameters()) {

                String[] navigationUriSplit = navigationUriType.getType().split("/");

                // The uri does not have the same size, go to next
                if (uriToFindSplit.length != navigationUriSplit.length) {
                    continue;
                }

                boolean uriMatched = true;
                int index = 0;
                while (index < navigationUriSplit.length && uriMatched) {
                    uriMatched = navigationUriSplit[index].equals(uriToFindSplit[index]) ||
                            (navigationUriSplit[index].startsWith("{") && navigationUriSplit[index].endsWith("}"));
                    index++;
                }

                if (uriMatched) {
                    return navigationUriType;
                }
            }
        }
        throw new UnknownNavigationUriException();
    }

    /**
     * Decode the uri to find the Navigation
     *
     * @param uri
     */
    public NavigationUri createNavigationUri(String uri) {
        try {
            NavigationUriType navigationUriType = parseNavigationUriType(uri);
            return new NavigationUri(navigationUriType);
        } catch (UnknownNavigationUriException e) {
            //FIXME handle that case
            return null;
        }
    }
}
