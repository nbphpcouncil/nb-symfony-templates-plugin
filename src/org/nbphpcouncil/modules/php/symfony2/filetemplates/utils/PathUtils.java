/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbphpcouncil.modules.php.symfony2.filetemplates.utils;

/**
 *
 * @author Monomachus
 */
class PathUtils {

    static String concat(String uriBundle, String anotherPath) {
        StringBuilder concatenatedPath = new StringBuilder(uriBundle);
        
        int endingCharsToRemove = getCharsNumberToRemove(uriBundle, PathDeletionOrderEnum.DESC);
        
        if (endingCharsToRemove > 0){
            concatenatedPath.delete(concatenatedPath.length() - endingCharsToRemove,
                    concatenatedPath.length());
        }
        
        int secondPathCharsToRemove = getCharsNumberToRemove(anotherPath, PathDeletionOrderEnum.ASC);
        
        String pathToAppend = anotherPath;
        
        if (secondPathCharsToRemove > 0){
            pathToAppend =  pathToAppend.substring(secondPathCharsToRemove - 1);
        }
        
        concatenatedPath.append(FileTemplatesUtils.UNIX_SEPARATOR);
        concatenatedPath.append(pathToAppend);
        
        return concatenatedPath.toString();
    }

    private static int getCharsNumberToRemove(String path, PathDeletionOrderEnum deletionOrder) {
        int charsToRemove = 0;
        int charPosition = -1;
        
        if (deletionOrder == PathDeletionOrderEnum.DESC){
            charPosition = path.length() - 1;
        } else {
            charPosition = 0;
        }
                
        while (path.charAt(charPosition) == '\\'){
            charsToRemove++;
            
            if (deletionOrder == PathDeletionOrderEnum.DESC){
                charPosition--;
            } else {
                charPosition++;
            }
        }
        
        return charsToRemove;
    }
    
}
