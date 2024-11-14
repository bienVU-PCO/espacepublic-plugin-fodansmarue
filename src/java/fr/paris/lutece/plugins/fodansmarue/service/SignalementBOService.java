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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import fr.paris.lutece.plugins.fodansmarue.business.dto.Source;
import fr.paris.lutece.plugins.fodansmarue.business.dto.TypeSignalement;
import fr.paris.lutece.plugins.fodansmarue.business.dto.TypeSignalementDTO;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Arrondissement;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Equipement;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.fodansmarue.business.entities.TypeEquipement;
import fr.paris.lutece.plugins.fodansmarue.commons.FunctionnalException;
import fr.paris.lutece.plugins.fodansmarue.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.fodansmarue.utils.constants.SignalementConstants;
import fr.paris.lutece.plugins.leaflet.modules.dansmarue.entities.Address;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

/**
 * SignalementConstants.
 */
public final class SignalementBOService implements ISignalementBOService
{

    /** The Constant ADRESSES. */
    private static final String ADRESSES = "adresses";

    /** The Constant PICTURES. */
    private static final String PICTURES = "pictures";

    /** The Constant ADDRESS. */
    private static final String ADDRESS = "address";

    /** The Constant N_ID_SIGNALEMENT. */
    private static final String N_ID_SIGNALEMENT = "nIdSignalement";

    /** The Constant ESPACE_PUBLIC. */
    private static final String ESPACE_PUBLIC = "Espace public";

    /**
     * Gets the geom from lambert to wgs 84.
     *
     * @param dLatLambert
     *            the d lat lambert
     * @param dLngLambert
     *            the d lng lambert
     * @return the geom from lambert to wgs 84
     */
    @Override
    public Double [ ] getGeomFromLambertToWgs84( Double dLatLambert, Double dLngLambert )
    {

        String response = null;
        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "dLatLambert", dLatLambert );
            jObject.put( "dLngLambert", dLngLambert );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_GEOM_FROM_LAMBER_TO_WS_84 ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructArrayType( Double.class ) );

        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getGeomFromLambertToWgs84", e );
        }

        return new Double [ 0];

    }

    /**
     * Gets the geom from lambert 93 to wgs 84.
     *
     * @param dLatLambert
     *            the d lat lambert
     * @param dLngLambert
     *            the d lng lambert
     * @return the geom from lambert 93 to wgs 84
     */
    @Override
    public Double [ ] getGeomFromLambert93ToWgs84( Double dLatLambert, Double dLngLambert )
    {

        String response = null;
        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "dLatLambert", dLatLambert );
            jObject.put( "dLngLambert", dLngLambert );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_GEOM_FROM_LAMBERT_93_TO_WS_84 ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructArrayType( Double.class ) );

        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getGeomFromLambert93ToWgs84", e );
        }

        return new Double [ 0];

    }

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
     */
    @Override
    public List<DossierSignalementDTO> findAllSignalementInPerimeterWithDTO( Double lat, Double lng, Integer radius )

    {

        String response;
        try
        {
            JSONObject jObject = new JSONObject( );

            jObject.put( "lng", lng );
            jObject.put( "lat", lat );
            jObject.put( "radius", radius );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_FIND_ALL_SIGNALEMENT_IN_PERIMETER_WITH_DTO ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( ).configure( Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructCollectionType( List.class, DossierSignalementDTO.class ) );

        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à findAllSignalementInPerimeterWithDTO", e );
        }

        return Collections.emptyList( );

    }

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
     */
    @Override
    public Integer getDistanceBetweenSignalement( double lat1, double lng1, double lat2, double lng2 )
    {

        String response;

        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "lat1", lat1 );
            jObject.put( "lat2", lat2 );
            jObject.put( "lng1", lng1 );
            jObject.put( "lng2", lng2 );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_DISTANCE_BETWEEN_SIGNALEMENT ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, Integer.class );

        }
        catch( JSONException | HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getDistanceBetweenSignalement", e );
        }

        return null;
    }

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
     */
    @Override
    public boolean isSignalementFollowableAndisSignalementFollowedByUser( int nIdSignalement, String userGuid, String choice )
    {

        String response;

        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( N_ID_SIGNALEMENT, nIdSignalement );
            if ( userGuid != null )
            {
                jObject.put( "userGuid", userGuid );
            }

            if ( choice.equals( ESPACE_PUBLIC ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.REST_IS_SIGNALEMENT_FOLLOWABLE ), jObject.toString( ), true );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.REST_IS_SIGNALEMENT_FOLLOWABLE ), jObject.toString( ), true );
            }

            ObjectMapper mapper = new ObjectMapper( );

            return mapper.readValue( response, Boolean.class );
        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à isSignalementFollowableAndisSignalementFollowedByUser", e );
        }

        return false;
    }

    /**
     * Gets the all priorite.
     *
     * @param choice
     *            the choice
     * @return the all priorite
     */
    @Override
    public List<Priorite> getAllPriorite( String choice )
    {
        String response = null;

        try
        {
            if ( choice.equals( ESPACE_PUBLIC ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_ALL_PRIORITE ), null, false );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_ALL_PRIORITE ), null, false );
            }

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructCollectionType( List.class, Priorite.class ) );
        }
        catch( HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getAllPriorite", e );
        }

        return Collections.emptyList( );

    }

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
    @Override
    public Priorite loadPrioriteById( long lId ) throws IOException, JSONException
    {

        String response;
        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "lId", lId );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_LOAD_PRIORITE_BY_ID ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, Priorite.class );

        }
        catch( HttpAccessException e )
        {
            AppLogService.error( "Erreur lors l'appel à loadPrioriteById", e );
        }

        return null;

    }

    /**
     * Gets the arrondissement by geom.
     *
     * @param lng
     *            the lng
     * @param lat
     *            the lat
     * @return the arrondissement by geom
     */
    @Override
    public Arrondissement getArrondissementByGeom( double lng, double lat )
    {

        String response;
        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "lng", lng );
            jObject.put( "lat", lat );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_ARRONDISSEMENT_BY_GEOM ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, Arrondissement.class );
        }
        catch( HttpAccessException | IOException | JSONException e )
        {
            AppLogService.error( "Erreur lors l'appel à getArrondissementByGeom", e );
        }

        return null;

    }

    /**
     * Gets the incidents by equipement.
     *
     * @param equipementId
     *            the equipement id
     * @return the incidents by equipement
     */
    @Override
    public List<DossierSignalementDTO> getIncidentsByEquipement( Long equipementId )
    {

        String response;
        try
        {
            JSONObject jObject = new JSONObject( );

            jObject.put( "request", SignalementConstants.REST_GET_INCIDENT_BY_EQUIPEMENT );
            jObject.put( "equipementId", equipementId );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_INCIDENT_BY_EQUIPEMENT ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            JsonNode json = mapper.readTree( response );

            List<DossierSignalementDTO> incidentsList = new ArrayList<>( );
            JsonNode jsonIncidents = json.findPath( "closest_incidents" );
            ObjectMapper incidentsMapper = new ObjectMapper( ).configure( Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );

            for ( JsonNode value : jsonIncidents )
            {
                // Incidents
                DossierSignalementDTO incident = incidentsMapper.readValue( value, DossierSignalementDTO.class );
                incident.setAdresse( value.get( ADDRESS ).getTextValue( ) );
                incident.setDateCreation( value.get( "date" ).getTextValue( ) );
                incident.setCommentaire( value.get( "descriptive" ).getTextValue( ) );
                incident.setHeureCreation( value.get( "hour" ).getTextValue( ) );
                incident.setLat( value.get( "lat" ).getDoubleValue( ) );
                incident.setLng( value.get( "lng" ).getDoubleValue( ) );
                incident.setIdCategory( value.get( "categoryId" ).getIntValue( ) );
                if ( value.get( PICTURES ).get( "close" ).get( 0 ) != null )
                {
                    incident.setImgUrl( value.get( PICTURES ).get( "close" ).get( 0 ).getTextValue( ) );

                }
                else
                    if ( value.get( PICTURES ).get( "far" ).get( 0 ) != null )
                    {
                        incident.setImgUrl( value.get( PICTURES ).get( "far" ).get( 0 ).getTextValue( ) );
                    }
                incidentsList.add( incident );

            }

            return incidentsList;

        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getIncidentsByEquipement", e );
        }

        return Collections.emptyList( );

    }

    /**
     * Gets the all equipements.
     *
     * @return the all equipements
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    @Override
    public List<TypeEquipement> getAllEquipements( ) throws IOException, JSONException
    {

        String response;
        try
        {

            JSONObject jObject = new JSONObject( );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_EQUIPEMENTS ), jObject.toString( ), false );

            ObjectMapper mapper = new ObjectMapper( );
            JsonNode json = mapper.readTree( response );

            List<TypeEquipement> typeEquipements = new ArrayList<>( );
            List<Equipement> equipements = new ArrayList<>( );
            Map<String, TypeEquipement> map = new HashMap<>( );

            JsonNode jsonEquipement = json.findPath( "equipements" );
            ObjectMapper typeEquipementMapper = new ObjectMapper( ).configure( Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            Iterator<Entry<String, JsonNode>> fields = jsonEquipement.getFields( );

            // Remplissage de la liste de TypeEquipement et de la liste Equipement
            while ( fields.hasNext( ) )
            {
                Entry<String, JsonNode> entry = fields.next( );
                String key = entry.getKey( );

                if ( !"0".equals( key ) )
                {
                    JsonNode value = entry.getValue( );

                    Integer parentId = value.get( "parent_id" ).getIntValue( );

                    if ( parentId.equals( 0 ) )
                    {
                        // TypeEquipement
                        TypeEquipement typeEquipement = typeEquipementMapper.readValue( value, TypeEquipement.class );
                        typeEquipement.setId( Long.parseLong( key ) );
                        typeEquipement.setLibelleEcranMobile( value.get( "libelleEcranMobile" ).getTextValue( ) );
                        typeEquipements.add( typeEquipement );

                        map.put( key, typeEquipement );

                    }
                    else
                    {
                        // Equipement
                        Equipement equipement = typeEquipementMapper.readValue( value, Equipement.class );
                        equipement.setId( Long.valueOf( key ) );
                        equipement.setParentId( Long.valueOf( parentId ) );
                        equipements.add( equipement );

                        if ( map.containsKey( parentId.toString( ) ) )
                        {
                            map.get( parentId.toString( ) ).getListEquipements( ).add( equipement );
                        }
                    }
                }
            }

            return typeEquipements;
        }
        catch( HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getAllEquipements", e );
        }

        return new ArrayList<>( );

    }

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
    @Override
    public void addFollower( Long signalementId, String guid, String strUDID, String email, String device, String userToken, boolean createUser, String choice )
            throws Exception

    {

        String response;

        Boolean isAddOk = null;

        try
        {

            JSONObject jObject = new JSONObject( );

            jObject.put( "signalementId", signalementId );
            jObject.put( "guid", guid );
            jObject.put( "strUDID", strUDID );
            jObject.put( "email", email );
            jObject.put( "device", device );
            jObject.put( "userToken", userToken );
            jObject.put( "createUser", createUser );

            if ( choice.equals( ESPACE_PUBLIC ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.REST_ADD_FOLLOWER ), jObject.toString( ), true );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.REST_ADD_FOLLOWER ), jObject.toString( ), true );
            }

            isAddOk = new JSONObject( response ).getBoolean( "isAddOk" );

        }
        catch( HttpAccessException | JSONException e )
        {
            AppLogService.error( "Erreur lors l'appel à addFollower", e );
        }

        if ( !isAddOk )
        {
            throw new FunctionnalException( "Erreur lors de l'ajout d'un follower" );
        }

    }

    /**
     * Gets the type signalement tree.
     *
     * @return the type signalement tree
     */
    @Override
    public List<TypeSignalementDTO> getTypeSignalementTree( )
    {
        try
        {

            String response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_TYPE_SIGNALEMENT_TREE ), null, false );

            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructCollectionType( List.class, TypeSignalementDTO.class ) );

        }
        catch( HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getTypeSignalementTree", e );
        }

        return Collections.emptyList( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeSignalementDTO> getTypeSignalementTreeForSource( Integer idSource )
    {
        try
        {
            String response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_TYPE_SIGNALEMENT_TREE_FOR_SOURCE ).concat( Integer.toString( idSource ) ), null, false );

            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructCollectionType( List.class, TypeSignalementDTO.class ) );

        }
        catch( HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getTypeSignalementTreeForSource", e );
        }

        return Collections.emptyList( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Source getInfosForSource( Integer idSource )
    {
        try
        {
            String response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_INFO_FOR_SOURCE ).concat( Integer.toString( idSource ) ), null, false );

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructType( Source.class ) );

        }
        catch( HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getTypeSignalementTreeForSource", e );
        }

        return null;
    }

    /**
     * Gets the type signalement tree equipement.
     *
     * @param typeEquipementId
     *            the type equipement id
     * @return the type signalement tree equipement
     * @throws JSONException
     *             the JSON exception
     */
    @Override
    public List<TypeSignalementDTO> getTypeSignalementTreeEquipement( Long typeEquipementId ) throws JSONException
    {

        try
        {

            JSONObject jObject = new JSONObject( );
            jObject.put( "typeEquipementId", typeEquipementId );

            String response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                    .concat( SignalementConstants.REST_GET_TYPE_SIGNALEMENT_TREE ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructCollectionType( List.class, TypeSignalementDTO.class ) );

        }
        catch( HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getTypeSignalementTree", e );
        }

        return Collections.emptyList( );

    }

    /**
     * Gets the type signalement.
     *
     * @param nIdSignalement
     *            the n id signalement
     * @param choice
     *            the choice
     * @return the type signalement
     */
    @Override
    public TypeSignalement getTypeSignalement( int nIdSignalement, String choice )
    {

        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( N_ID_SIGNALEMENT, nIdSignalement );

            String response = "";

            if ( choice.equals( ESPACE_PUBLIC ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_TYPE_SIGNALEMENT ), jObject.toString( ), true );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_TYPE_SIGNALEMENT ), jObject.toString( ), true );
            }
            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            return mapper.readValue( response, TypeSignalement.class );
        }
        catch( JSONException | HttpAccessException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getTypeSignalement", e );
        }

        return null;
    }

    /**
     * Find by id type signalement.
     *
     * @param nIdSignalement
     *            the n id signalement
     * @return the type signalement
     */
    @Override
    public TypeSignalement findByIdTypeSignalement( int nIdSignalement )
    {

        String response;
        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( N_ID_SIGNALEMENT, nIdSignalement );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.REST_FIND_BY_ID_TYPE_SIGNALEMENT ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            return mapper.readValue( response, TypeSignalement.class );

        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à findByIdTypeSignalement", e );
        }

        return null;
    }

    /**
     * Gets the address item.
     *
     * @param address
     *            the address
     * @return the address item
     */
    @Override
    public List<Address> getAddressItem( String address )
    {

        String response;
        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( ADDRESS, address );

            response = callWSBOSignalement(
                    AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL ).concat( SignalementConstants.GET_ADRESSE_ITEM ),
                    jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            return mapper.readValue( response, mapper.getTypeFactory( ).constructCollectionType( List.class, Address.class ) );

        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getAddressItem", e );
        }

        return Collections.emptyList( );
    }

    /**
     * Gets the dossiers courrants by geom with limit.
     *
     * @param longitude
     *            the longitude
     * @param latitude
     *            the latitude
     * @return the dossiers courrants by geom with limit
     */
    @Override
    public List<DossierSignalementDTO> getDossiersCourrantsByGeomWithLimit( Double longitude, Double latitude )
    {

        String response;
        try
        {

            JSONObject jObject = new JSONObject( );
            jObject.put( "longitude", longitude );
            jObject.put( "latitude", latitude );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.GET_DOSSIERS_COURRANTS_BY_GEOM_WITH_LIMIT ), jObject.toString( ), true );

            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );

            return mapper.readValue( response, mapper.getTypeFactory( ).constructCollectionType( List.class, DossierSignalementDTO.class ) );

        }
        catch( HttpAccessException | JSONException | IOException | NullPointerException e )
        {
            AppLogService.error( "Erreur lors l'appel à getDossiersCourrantsByGeomWithLimit", e );
        }

        return Collections.emptyList( );
    }

    /**
     * Gets the signalement by token.
     *
     * @param token
     *            the token
     * @param instance
     *            the instance
     * @return the signalement by token
     */
    @Override
    public Signalement getSignalementByToken( String token, String instance )
    {

        String response;

        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "token", token );

            if ( instance.equals( SignalementConstants.PROPERTY_SIGNALEMENT ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_SIGNALEMENT_BY_TOKEN ), jObject.toString( ), true );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_SIGNALEMENT_BY_TOKEN ), jObject.toString( ), true );
            }

            if ( response != null )
            {
                ObjectMapper mapper = new ObjectMapper( );
                mapper.configure( Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
                JsonNode json = mapper.readTree( response );

                Signalement signalement = new Signalement( );
                Adresse adr = new Adresse( );
                List<Adresse> adresses = new ArrayList<>( );
                Equipement equipement = new Equipement( );

                adr.setId( json.findPath( ADRESSES ).get( "id" ).getLongValue( ) );
                adr.setAdresse( json.findPath( ADRESSES ).get( ADDRESS ).getTextValue( ) );
                adr.setLat( json.findPath( ADRESSES ).get( "lat" ).getDoubleValue( ) );
                adr.setLng( json.findPath( ADRESSES ).get( "lng" ).getDoubleValue( ) );
                adresses.add( adr );

                signalement.setId( json.get( "id" ).getLongValue( ) );
                signalement.setAdresses( adresses );
                signalement.setPrefix( json.get( "prefix" ).getTextValue( ) );
                signalement.setAnnee( json.get( "annee" ).getIntValue( ) );
                signalement.setMois( json.get( "mois" ).getTextValue( ) );
                signalement.setNumero( json.get( "numero" ).getIntValue( ) );
                signalement.setTypeSignalement( mapper.readValue( json.get( "typeSignalement" ), TypeSignalement.class ) );
                signalement.setNombreFeedback( json.get( "nombreFeedback" ).getIntValue( ) );

                if ( !instance.equals( SignalementConstants.PROPERTY_SIGNALEMENT ) )
                {
                    equipement.setId( json.findPath( "equipement" ).get( "id" ).getLongValue( ) );
                    equipement.setName( json.findPath( "equipement" ).get( "libelle" ).getTextValue( ) );
                    signalement.setEquipement( equipement );
                }

                return signalement;
            }
        }
        catch( HttpAccessException | JSONException | IOException e )
        {
            AppLogService.error( "Erreur lors l'appel à getSignalementByToken", e );
        }

        return null;
    }

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
    @Override
    public String getHistorySignalement( Integer idSignalement, String instance ) throws IOException
    {

        String response;

        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "idSignalement", idSignalement );

            if ( instance.equals( SignalementConstants.PROPERTY_SIGNALEMENT ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_HISTORY_SIGNALEMENT ), jObject.toString( ), true );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.REST_GET_HISTORY_SIGNALEMENT ), jObject.toString( ), true );
            }

            return response;
        }
        catch( HttpAccessException | JSONException e )
        {
            AppLogService.error( "Erreur lors l'appel à getHistorySignalement", e );
        }

        return null;
    }

    /**
     * Validate service fait signalement by token.
     *
     * @param token
     *            the token
     * @param instance
     *            the instance
     * @return true, if successful
     */
    @Override
    public boolean validateServiceFaitSignalementByToken( String token, String instance )
    {
        boolean serviceFait = false;
        String response;

        try
        {
            JSONObject jObject = new JSONObject( );
            jObject.put( "token", token );

            if ( instance.equals( SignalementConstants.PROPERTY_SIGNALEMENT ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.REST_SERVICE_FAIT_SIGNALEMENT ), jObject.toString( ), true );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.REST_SERVICE_FAIT_SIGNALEMENT ), jObject.toString( ), true );
            }

            serviceFait = new JSONObject( response ).getBoolean( "isServiceFait" );

            return serviceFait;

        }
        catch( HttpAccessException | JSONException e )
        {
            AppLogService.error( "Erreur lors l'appel à validateServiceFaitSignalement", e );
        }

        return serviceFait;
    }

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
    @Override
    public JSONObject sauvegarderSignalement( Signalement demandeSignalement, LuteceUser user, String choice ) throws IOException, JSONException
    {
        String response;
        try
        {
            JSONObject jObject = new JSONObject( );

            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            demandeSignalement.setUnit( null );
            demandeSignalement.getPhotos( ).forEach( photo -> photo.setSignalement( null ) );

            jObject.put( "demandeSignalement", mapper.writerWithDefaultPrettyPrinter( ).writeValueAsString( demandeSignalement ) );
            if ( user != null )
            {
                jObject.put( "userName", user.getName( ) );
                jObject.put( "userMail", user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ) );
            }

            if ( ESPACE_PUBLIC.equals( choice ) )
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                        .concat( SignalementConstants.SAVE_SIGNALEMENT ), jObject.toString( ), true );

            }
            else
            {
                response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_EQUIPEMENT_BO_URL )
                        .concat( SignalementConstants.SAVE_SIGNALEMENT ), jObject.toString( ), true );
            }
            return new JSONObject( response );

        }
        catch( HttpAccessException e )
        {
            AppLogService.error( "Erreur lors l'appel à sauvegarderSignalement", e );
        }

        JSONObject jObjectResponse = new JSONObject( );
        jObjectResponse.put( SignalementConstants.RETOUR_CREATION_SIGNALEMENT, false );
        return jObjectResponse;
    }

    /**
     * Call BO Signalement Rest Service.
     *
     * @param restUrl
     *            Url to call
     * @param jsonStr
     *            params in json format
     * @param isPost
     *            call WS in post or get mode
     * @return WS response in Json format
     * @throws HttpAccessException
     *             the http access exception
     */
    private String callWSBOSignalement( String restUrl, String jsonStr, boolean isPost ) throws HttpAccessException
    {

        HttpAccess http = new HttpAccess( );

        Map<String, String> headersRequest = new HashMap<>( );
        headersRequest.put( "Content-Type", "application/json" );
        Map<String, String> headersResponse = new HashMap<>( );

        if ( isPost )
        {
            return http.doPostJSON( restUrl, jsonStr, headersRequest, headersResponse );
        }
        else
        {
            return http.doGet( restUrl );
        }

    }

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
    @Override
    public JSONObject sauvegarderReponsesFormulaireSatisfaction( String strToken, String strChoixFormulaire, String strCommentaireSatisfaction ) throws JSONException
    {
        String response;
        try
        {
            JSONObject jObject = new JSONObject( );

            ObjectMapper mapper = new ObjectMapper( );
            mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );

            jObject.put( "token", strToken );
            jObject.put( "choix", strChoixFormulaire );
            jObject.put( "commentaire", strCommentaireSatisfaction );

            response = callWSBOSignalement( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_REST_SIGNALEMENT_BO_URL )
                    .concat( SignalementConstants.SAVE_SATISFACTION_FORM_ANSWER), jObject.toString( ), true );

            return new JSONObject( response );
        }
        catch( HttpAccessException e )
        {
            AppLogService.error( "Erreur lors l'appel à saveSatisfactionFormAnswer", e );
        }

        JSONObject jObjectResponse = new JSONObject( );
        jObjectResponse.put( SignalementConstants.SAVE_SATISFACTION_FORM_ANSWER, false );
        return jObjectResponse;
    }

}
