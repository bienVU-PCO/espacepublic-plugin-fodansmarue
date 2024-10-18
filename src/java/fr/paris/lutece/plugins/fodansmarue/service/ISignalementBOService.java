/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.fodansmarue.service;

import java.io.IOException;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import fr.paris.lutece.plugins.fodansmarue.business.dto.Source;
import fr.paris.lutece.plugins.fodansmarue.business.dto.TypeSignalement;
import fr.paris.lutece.plugins.fodansmarue.business.dto.TypeSignalementDTO;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.fodansmarue.business.entities.TypeEquipement;
import fr.paris.lutece.plugins.fodansmarue.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.leaflet.modules.dansmarue.entities.Address;
import fr.paris.lutece.portal.service.security.LuteceUser;

/**
 * The Interface ISignalementBOService.
 */
public interface ISignalementBOService
{

    /**
     * Gets the geom from lambert to wgs 84.
     *
     * @param dLatLambert
     *            the d lat lambert
     * @param dLngLambert
     *            the d lng lambert
     * @return the geom from lambert to wgs 84
     * @throws JSONException
     *             the JSON exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    Double [ ] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert ) throws JSONException, IOException;

    /**
     * Gets the geom from lambert 93 to wgs 84.
     *
     * @param dLatLambert
     *            the d lat lambert
     * @param dLngLambert
     *            the d lng lambert
     * @return the geom from lambert 93 to wgs 84
     * @throws JSONException
     *             the JSON exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    Double [ ] getGeomFromLambert93ToWgs84( Double dLatLambert, Double dLngLambert ) throws JSONException, IOException;

    /**
     * Find all signalement in perimeter with DTO.
     *
     * @param lat
     *            the lat
     * @param lng
     *            the lng
     * @param radius
     *            the radius
     * @return the list
     * @throws JSONException
     *             the JSON exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius ) throws JSONException, IOException;

    /**
     * Gets the distance between signalement.
     *
     * @param lat1
     *            the lat 1
     * @param lng1
     *            the lng 1
     * @param lat2
     *            the lat 2
     * @param lng2
     *            the lng 2
     * @return the distance between signalement
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    Integer getDistanceBetweenSignalement( double lat1, double lng1, double lat2, double lng2 ) throws IOException, JSONException;

    /**
     * Checks if is signalement followable andis signalement followed by user.
     *
     * @param nIdSignalement
     *            the n id signalement
     * @param userGuid
     *            the user guid
     * @param choice
     *            the choice
     * @return true, if is signalement followable andis signalement followed by user
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    boolean isSignalementFollowableAndisSignalementFollowedByUser( int nIdSignalement, String userGuid, String choice ) throws IOException, JSONException;

    /**
     * Gets the all priorite.
     *
     * @param choice
     *            the choice
     * @return the all priorite
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    List<Priorite> getAllPriorite( String choice ) throws IOException;

    /**
     * Load priorite by id.
     *
     * @param lId
     *            the l id
     * @return the priorite
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    Priorite loadPrioriteById( long lId ) throws IOException, JSONException;

    /**
     * Gets the arrondissement by geom.
     *
     * @param lng
     *            the lng
     * @param lat
     *            the lat
     * @return the arrondissement by geom
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    Arrondissement getArrondissementByGeom( double lng, double lat ) throws IOException, JSONException;

    /**
     * Find by id type signalement.
     *
     * @param nIdSignalement
     *            the n id signalement
     * @return the type signalement
     * @throws JSONException
     *             the JSON exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    TypeSignalement findByIdTypeSignalement( int nIdSignalement ) throws JSONException, IOException;

    /**
     * Gets the address item.
     *
     * @param address
     *            the address
     * @return the address item
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    List<Address> getAddressItem( String address ) throws IOException, JSONException;

    /**
     * Gets the dossiers courrants by geom with limit.
     *
     * @param longitude
     *            the longitude
     * @param latitude
     *            the latitude
     * @return the dossiers courrants by geom with limit
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    List<DossierSignalementDTO> getDossiersCourrantsByGeomWithLimit( Double longitude, Double latitude ) throws IOException, JSONException;

    /**
     * Gets the all equipements.
     *
     * @return the all equipements
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    List<TypeEquipement> getAllEquipements( ) throws IOException, JSONException;

    /**
     * Gets the incidents by equipement.
     *
     * @param equipementId
     *            the equipement id
     * @return the incidents by equipement
     */
    List<DossierSignalementDTO> getIncidentsByEquipement( Long equipementId );

    /**
     * Gets the type signalement.
     *
     * @param nIdSignalement
     *            the n id signalement
     * @param choice
     *            the choice
     * @return the type signalement
     */
    TypeSignalement getTypeSignalement( int nIdSignalement, String choice );

    /**
     * Gets the type signalement tree equipement.
     *
     * @param typeEquipementId
     *            the type equipement id
     * @return the type signalement tree equipement
     * @throws JSONException
     *             the JSON exception
     */
    List<TypeSignalementDTO> getTypeSignalementTreeEquipement( Long typeEquipementId ) throws JSONException;

    /**
     * Gets the type signalement tree.
     *
     * @return the type signalement tree
     */
    List<TypeSignalementDTO> getTypeSignalementTree( );

    /**
     * Gets the type signalement tree for source.
     *
     * @param idSource
     *            the id source
     * @return the type signalement tree for source
     */
    List<TypeSignalementDTO> getTypeSignalementTreeForSource( Integer idSource );

    /**
     * Gets the infos for source.
     *
     * @param idSource
     *            the id source
     * @return the infos for source
     */
    Source getInfosForSource( Integer idSource );

    /**
     * Sauvegarder signalement.
     *
     * @param demandeSignalement
     *            the demande signalement
     * @param user
     *            the user
     * @param choice
     *            the choice
     * @return the JSON object
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    JSONObject sauvegarderSignalement( Signalement demandeSignalement, LuteceUser user, String choice ) throws IOException, JSONException;

    /**
     * Adds the follower.
     *
     * @param signalementId
     *            the signalement id
     * @param guid
     *            the guid
     * @param strUDID
     *            the str UDID
     * @param email
     *            the email
     * @param device
     *            the device
     * @param userToken
     *            the user token
     * @param createUser
     *            the create user
     * @param choice
     *            the choice
     * @throws Exception
     *             the exception
     */
    void addFollower( Long signalementId, String guid, String strUDID, String email, String device, String userToken, boolean createUser, String choice )
            throws Exception;

    /**
     * Gets the signalement by token.
     *
     * @param token
     *            the token
     * @param instance
     *            the instance
     * @return the signalement by token
     */
    Signalement getSignalementByToken( String token, String instance );

    /**
     * Gets the history signalement.
     *
     * @param idSignalement
     *            the id signalement
     * @param instance
     *            the instance
     * @return the history signalement
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    String getHistorySignalement( Integer idSignalement, String instance ) throws IOException;

    /**
     * Validate service fait signalement by token.
     *
     * @param token
     *            the token
     * @param instance
     *            the instance
     * @return true, if successful
     */
    boolean validateServiceFaitSignalementByToken( String token, String instance );


    /**
     * Update signalement with satisfaction form answer by token.
     *
     * @param strToken
     *            the token
     * @param strChoixFormulaire
     *            the answer of the satisfaction form
     * @param strCommentaireSatisfaction
     *            the comment of the satisfaction form
     * @return true, if successful
     */
    JSONObject sauvegarderReponsesFormulaireSatisfaction( String strToken, String strChoixFormulaire, String strCommentaireSatisfaction ) throws IOException, JSONException;

}
