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
package fr.paris.lutece.plugins.fodansmarue.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import fr.paris.lutece.plugins.fodansmarue.business.dto.Source;
import fr.paris.lutece.plugins.fodansmarue.business.dto.TypeSignalement;
import fr.paris.lutece.plugins.fodansmarue.business.dto.TypeSignalementDTO;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Adresse;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Equipement;
import fr.paris.lutece.plugins.fodansmarue.business.entities.PhotoDMR;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Priorite;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Signaleur;
import fr.paris.lutece.plugins.fodansmarue.business.entities.TypeEquipement;
import fr.paris.lutece.plugins.fodansmarue.dto.DossierSignalementDTO;
import fr.paris.lutece.plugins.fodansmarue.service.AdresseService;
import fr.paris.lutece.plugins.fodansmarue.utils.DmrUtils;
import fr.paris.lutece.plugins.fodansmarue.utils.ListUtils;
import fr.paris.lutece.plugins.fodansmarue.utils.constants.SignalementConstants;
import fr.paris.lutece.plugins.fodansmarue.utils.validator.ValidatorFinalisation;
import fr.paris.lutece.plugins.fodansmarue.utils.validator.ValidatorLocalisation;
import fr.paris.lutece.plugins.fodansmarue.utils.validator.ValidatorSignaleur;
import fr.paris.lutece.plugins.leaflet.modules.dansmarue.entities.Address;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class provides a simple implementation of an XPage.
 */
@Controller( xpageName = "fodansmarue", pageTitleI18nKey = "fodansmarue.xpage.dansmarue.pageTitle", pagePathI18nKey = "fodansmarue.xpage.dansmarue.pagePathLabel" )
public class XPageDansMaRue extends AbstractXPage
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7782392238698476108L;

    /** The Constant TEMPLATE_XPAGE_ACCUEIL. */
    // TEMPLATES
    private static final String TEMPLATE_XPAGE_ACCUEIL = "/skin/plugins/fosignalement/accueil.html";

    /** The Constant TEMPLATE_XPAGE_ADRESSE. */
    private static final String TEMPLATE_XPAGE_ADRESSE = "/skin/plugins/fosignalement/adresse.html";

    /** The Constant TEMPLATE_XPAGE_DOUBLONS. */
    private static final String TEMPLATE_XPAGE_DOUBLONS = "/skin/plugins/fosignalement/doublons.html";

    /** The Constant TEMPLATE_XPAGE_CATEGORIE. */
    private static final String TEMPLATE_XPAGE_CATEGORIE = "/skin/plugins/fosignalement/type_signalement.html";

    /** The Constant TEMPLATE_XPAGE_FINALISATION. */
    private static final String TEMPLATE_XPAGE_FINALISATION = "/skin/plugins/fosignalement/finalisation.html";

    /** The Constant TEMPLATE_XPAGE_SUIVI. */
    private static final String TEMPLATE_XPAGE_SUIVI = "/skin/plugins/fosignalement/suivi_signalement.html";

    /** The Constant TEMPLATE_XPAGE_CONFIRMATION. */
    private static final String TEMPLATE_XPAGE_CONFIRMATION = "/skin/plugins/fosignalement/confirmation.html";

    /** The Constant VIEW_DOUBLONS. */
    // VIEW
    private static final String VIEW_DOUBLONS = "doublons";

    /** The Constant VIEW_CATEGORIE. */
    private static final String VIEW_CATEGORIE = "categorie";

    /** The Constant VIEW_FINALISATION. */
    private static final String VIEW_FINALISATION = "finalisation";

    /** The Constant VIEW_SUIVI_SIGNALEMENT. */
    private static final String VIEW_SUIVI_SIGNALEMENT = "suivi_signalement";

    /** The Constant VIEW_CONFIRMATION. */
    private static final String VIEW_CONFIRMATION = "confirmation";

    /** The Constant VIEW_ADRESSE_ANOMALIE. */
    private static final String VIEW_ADRESSE_ANOMALIE = "adresse";

    /** The Constant ACTION_SEARCH_ADDRESS. */
    // ACTIONS
    private static final String ACTION_SEARCH_ADDRESS = "search_address";

    /** The Constant ACTION_VALIDATE_DECLARATION. */
    private static final String ACTION_VALIDATE_DECLARATION = "validate_declaration";

    /** The Constant ACTION_VALIDATE_ADDRESS. */
    private static final String ACTION_VALIDATE_ADDRESS = "validate_address";

    /** The Constant ACTION_VALIDATE_EQUIPEMENT. */
    private static final String ACTION_VALIDATE_EQUIPEMENT = "validate_equipement";

    /** The Constant ACTION_VALIDATE_DOUBLONS. */
    private static final String ACTION_VALIDATE_DOUBLONS = "validate_doublons";

    /** The Constant ACTION_VALIDATE_CATEGORIE. */
    private static final String ACTION_VALIDATE_CATEGORIE = "validate_categorie";

    /** The Constant ACTION_VALIDATE_FINALISATION. */
    private static final String ACTION_VALIDATE_FINALISATION = "validate_finalisation";

    /** The Constant ACTION_VALIDATE_SUIVI_SIGNALEMENT. */
    private static final String ACTION_VALIDATE_SUIVI_SIGNALEMENT = "validate_suivi_signalement";

    /** The Constant ACTION_VALIDATE_SIGNALEMENT. */
    private static final String ACTION_VALIDATE_SIGNALEMENT = "validate_signalement";

    /** The Constant ACTION_DOWNLOAD. */
    private static final String ACTION_DOWNLOAD = "download";

    /** The Constant ACTION_RETOUR_ACCUEIL. */
    private static final String ACTION_RETOUR_ACCUEIL = "retour_accueil";

    /** The Constant PARAMETER_ADDRESS. */
    // PARAMETERS
    private static final String PARAMETER_ADDRESS = "adresse";

    /** The Constant PARAMETER_TYPE_EQUIPEMENT. */
    private static final String PARAMETER_TYPE_EQUIPEMENT = "typeEquipement";

    /** The Constant PARAMETER_EQUIPEMENT. */
    private static final String PARAMETER_EQUIPEMENT = "validEquipement";

    /** The Constant PARAMETER_WITHOUT_JS. */
    private static final String PARAMETER_WITHOUT_JS = "withoutJS";

    /** The Constant PARAMETER_VALID_ADDRESS. */
    private static final String PARAMETER_VALID_ADDRESS = "validAddress";

    /** The Constant PARAMETER_TYPE_SIGNALEMENT_ID. */
    private static final String PARAMETER_TYPE_SIGNALEMENT_ID = "typeSignalementId";

    /** The Constant PARAMETER_TYPE_SIGNALEMENT. */
    private static final String PARAMETER_TYPE_SIGNALEMENT = "typeSignalement";

    /** The Constant PARAMETER_PRIORITE. */
    private static final String PARAMETER_PRIORITE = "priorite";

    /** The Constant PARAMETER_COMMENTAIRE. */
    private static final String PARAMETER_COMMENTAIRE = "commentaire";

    /** The Constant PARAMETER_SIGNALEMENT_ID. */
    private static final String PARAMETER_SIGNALEMENT_ID = "signalementId";

    /** The Constant PARAMETER_PHOTO_DETAILLEE. */
    private static final String PARAMETER_PHOTO_DETAILLEE = "photo_detaillee";

    /** The Constant PARAMETER_PHOTO_DENSEMBLE. */
    private static final String PARAMETER_PHOTO_DENSEMBLE = "photo_ensemble";

    /** The Constant PARAMETER_EMAIL. */
    private static final String PARAMETER_EMAIL = "email";

    /** The Constant PARAMETER_SAUVEGARDE_SIGNALEMENT. */
    private static final String PARAMETER_SAUVEGARDE_SIGNALEMENT = "sauvegardeSignalement";

    /** The Constant PARAMETER_ID_SOURCE. */
    private static final String PARAMETER_ID_SOURCE = "idSource";

    /** The Constant PARAMETER_REF_ITEM_SOURCE. */
    private static final String PARAMETER_REF_ITEM_SOURCE = "refItemSource";

    /** The Constant PARAMETER_POSITION_X. */
    private static final String PARAMETER_POSITION_X = "x";

    /** The Constant PARAMETER_POSITION_Y. */
    private static final String PARAMETER_POSITION_Y = "y";

    /** The Constant MARK_PROPOSED_ADDRESSES. */
    // MARKERS
    public static final String MARK_PROPOSED_ADDRESSES = "proposedAddresses";

    /** The Constant MARK_PROPOSED_EQUIPEMENT. */
    public static final String MARK_PROPOSED_EQUIPEMENT = "proposedEquipements";

    /** The Constant MARK_NO_VALID_ADDRESSES. */
    public static final String MARK_NO_VALID_ADDRESSES = "noValidAddresses";

    /** The Constant MARK_CHOICE. */
    public static final String MARK_CHOICE = "choix_declaration";

    /** The Constant MARK_SIGNALEMENT. */
    private static final String MARK_SIGNALEMENT = "signalementFOBean";

    /** The Constant MARK_ADRESSE. */
    private static final String MARK_ADRESSE = "adresse";

    /** The Constant MARK_TYPE_EQUIPEMENT_LIST. */
    private static final String MARK_TYPE_EQUIPEMENT_LIST = "typeEquipementList";

    /** The Constant MARK_IS_EQUIPEMENT_UP. */
    private static final String MARK_IS_EQUIPEMENT_UP = "isEquipementUp";

    /** The Constant MARK_TYPE_EQUIPEMENT_ID. */
    private static final String MARK_TYPE_EQUIPEMENT_ID = "typeEquipementId";

    /** The Constant MARK_TYPE_EQUIPEMENT_LIBELLE. */
    private static final String MARK_TYPE_EQUIPEMENT_LIBELLE = "typeEquipementLibelle";

    /** The Constant MARK_EQUIPEMENT. */
    private static final String MARK_EQUIPEMENT = "equipement";

    /** The Constant MARK_EQUIPEMENT_LIST. */
    private static final String MARK_EQUIPEMENT_LIST = "equipementsList";

    /** The Constant MARK_LIST_DOUBLONS. */
    private static final String MARK_LIST_DOUBLONS = "list_doublons";

    /** The Constant MARK_TYPE_SIGNALEMENT. */
    private static final String MARK_TYPE_SIGNALEMENT = "typeSignalement";

    /** The Constant MARK_LIST_PHOTO_DETAILLEE. */
    private static final String MARK_LIST_PHOTO_DETAILLEE = "listupload_photo_detaillee";

    /** The Constant MARK_LIST_PHOTO_ENSEMBLE. */
    private static final String MARK_LIST_PHOTO_ENSEMBLE = "listupload_photo_ensemble";

    /** The Constant MARK_PHOTO_ENSEMBLE. */
    private static final String MARK_PHOTO_ENSEMBLE = "photo_ensemble";

    /** The Constant MARK_PHOTO_DETAILLEE. */
    private static final String MARK_PHOTO_DETAILLEE = "photo_detaillee";

    /** The Constant MARK_EMAIL. */
    private static final String MARK_EMAIL = "email";

    /** The Constant MARK_NOT_SIGNED_IN. */
    private static final String MARK_NOT_SIGNED_IN = "not_signed_in";

    /** The Constant CHOICE_ESPACE_PUBLIC. */
    // CHOICE
    public static final String CHOICE_ESPACE_PUBLIC = "Espace public";

    /** The Constant CHOICE_EQUIPEMENT. */
    public static final String CHOICE_EQUIPEMENT = "Equipement municipal";

    /** The Constant JSON_KEY_ID. */
    private static final String JSON_KEY_ID = "id";

    /** The adresse service. */
    // SERVICE
    private transient AdresseService _adresseService = SpringContextService.getBean( "adresseService" );

    /** The Constant MESSAGE_ERROR_LOCALISATION. */
    // MESSAGES
    private static final String MESSAGE_ERROR_LOCALISATION = "fodansmarue.etape.localisation.error.horsparis";

    /** The Constant MESSAGE_ERROR_NO_STREET_NUMBER. */
    private static final String MESSAGE_ERROR_NO_STREET_NUMBER = "fodansmarue.etape.localisation.error.numero.adresse";

    /** The Constant MESSAGE_ERROR_EQUIPEMENT. */
    private static final String MESSAGE_ERROR_EQUIPEMENT = "fodansmarue.etape.localisation.error.equipement";

    /** The lutece user. */
    private LuteceUser _luteceUser;

    /** The signalement. */
    private Signalement _signalement;

    /** The type equipement. */
    private TypeEquipement _typeEquipement;

    /** The signaleur. */
    private Signaleur _signaleur;

    /** The photos. */
    private List<PhotoDMR> _photos = new ArrayList<>( );

    /** The type equipement list. */
    private List<TypeEquipement> _typeEquipementList = new ArrayList<>( );

    /** The equipement list. */
    private List<Equipement> _equipementList = new ArrayList<>( );

    /** The list photos detaillee. */
    private List<FileItem> _listPhotosDetaillee = new ArrayList<>( );

    /** The list photos ensemble. */
    private List<FileItem> _listPhotosEnsemble = new ArrayList<>( );

    /** The b is from another source. */
    private boolean _bIsFromAnotherSource = false;

    /** The str ref item source. */
    private String _strRefItemSource;

    /** The source. */
    private Source _source;

    /** The choice. */
    private String _choice;

    /** The Constant PARAMETER_FIELD_NAME. */
    /*
     * Constantes pour le download
     */
    public static final String PARAMETER_FIELD_NAME = "fieldName";

    /**
     * Checks if is user connected.
     *
     * @param request
     *            the request
     * @return true, if is user connected
     * @throws UserNotSignedException
     *             the user not signed exception
     */
    protected boolean isUserConnected( HttpServletRequest request ) throws UserNotSignedException
    {
        // Vérifie que l'utilisateur est bien connecté
        boolean connected = false;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            _luteceUser = SecurityService.getInstance( ).getRemoteUser( request );
            connected = ( _luteceUser != null );
        }
        return connected;
    }

    /**
     * Returns the content of the page accueil.
     *
     * @param request
     *            The HTTP request
     * @return The view
     * @throws UserNotSignedException
     *             {@link UserNotSignedException}
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    @View( value = AbstractXPage.XPAGE_DANSMARUE, defaultView = true )
    public XPage viewAccueil( HttpServletRequest request ) throws UserNotSignedException, IOException, JSONException
    {

        Map<String, Object> model = getModel( );
        _luteceUser = getUser( request, false );

        _typeEquipementList = _signalementBoService.getAllEquipements( );
        _photos = new ArrayList<>( );

        ReferenceList refListTypeEquipement = ListUtils.toReferenceList( _typeEquipementList, JSON_KEY_ID, "libelleEcranMobile", null );

        ReferenceItem refItem = new ReferenceItem( );
        refItem.setCode( Long.toString( -1 ) );
        refItem.setName( AppPropertiesService.getProperty( SignalementConstants.PROPERTY_ESPACE_PUBLIC_VALUE ) );
        refListTypeEquipement.add( 0, refItem );

        model.put( MARK_TYPE_EQUIPEMENT_LIST, refListTypeEquipement );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        model.put( MARK_IS_EQUIPEMENT_UP, !_typeEquipementList.isEmpty( ) );
        return getXPage( TEMPLATE_XPAGE_ACCUEIL, request.getLocale( ), model );
    }

    /**
     * Get the equipement type and return the address page.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_VALIDATE_DECLARATION )
    public XPage validateDeclaration( HttpServletRequest request )
    {
        Long typeEquipementId = (long) -1;

        if ( ( request.getParameter( PARAMETER_TYPE_EQUIPEMENT ) != null ) && ( Long.parseLong( request.getParameter( PARAMETER_TYPE_EQUIPEMENT ) ) != -1 ) )
        {

            typeEquipementId = Long.parseLong( request.getParameter( PARAMETER_TYPE_EQUIPEMENT ) );

            for ( TypeEquipement te : _typeEquipementList )
            {
                if ( te.getId( ).longValue( ) == typeEquipementId.longValue( ) )
                {
                    _equipementList.clear( );
                    _equipementList.addAll( te.getListEquipements( ) );
                    _typeEquipement = te;
                    break;
                }
            }

            _choice = CHOICE_EQUIPEMENT;
        }
        else
        {
            _typeEquipement = new TypeEquipement( );
            _typeEquipement.setId( typeEquipementId );
            _choice = CHOICE_ESPACE_PUBLIC;
        }
        return redirectView( request, VIEW_ADRESSE_ANOMALIE );
    }

    /**
     * View address.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws UserNotSignedException
     *             the user not signed exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    @View( VIEW_ADRESSE_ANOMALIE )
    public XPage viewAddress( HttpServletRequest request ) throws UserNotSignedException, IOException, JSONException
    {
        if ( _typeEquipement == null )
        {
            return viewAccueil( request );
        }
        Map<String, Object> model = getModel( );

        DmrUtils.formatStringManual( _equipementList );

        model.put( MARK_TYPE_EQUIPEMENT_ID, _typeEquipement.getId( ) );
        model.put( MARK_TYPE_EQUIPEMENT_LIBELLE, _typeEquipement.getLibelleEcranMobile( ) );
        model.put( MARK_CHOICE, _choice );
        model.put( MARK_EQUIPEMENT_LIST, _equipementList );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        request.getSession( ).setAttribute( MARK_MAP_ERRORS, null );
        return getXPage( TEMPLATE_XPAGE_ADRESSE, request.getLocale( ), model );

    }

    /**
     * Validate equipement.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_VALIDATE_EQUIPEMENT )
    public XPage validateEquipement( HttpServletRequest request )
    {
        Equipement equipement = new Equipement( );
        String paramEquipementId = request.getParameter( PARAMETER_EQUIPEMENT );

        if ( ( paramEquipementId != null ) && !"".equals( paramEquipementId ) )
        {
            Long equipementId = Long.parseLong( paramEquipementId );

            for ( Equipement eq : _equipementList )
            {
                if ( equipementId.longValue( ) == eq.getId( ).longValue( ) )
                {
                    equipement = eq;
                    break;
                }
            }
        }
        else
        {
            Map<String, String> errors = new HashMap<>( );

            errors.put( PARAMETER_ADDRESS, I18nService.getLocalizedString( MESSAGE_ERROR_EQUIPEMENT, request.getLocale( ) ) );
            request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
            return redirectView( request, VIEW_ADRESSE_ANOMALIE );
        }

        _signalement = new Signalement( );
        _signalement.setEquipement( equipement );

        return redirectView( request, VIEW_DOUBLONS );
    }

    /**
     * Displayed in accessibility mode, fills the proposed address list.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_SEARCH_ADDRESS )
    public XPage searchAddress( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        String address = request.getParameter( PARAMETER_ADDRESS );
        Adresse adresse = new Adresse( );
        adresse.setAdresse( address );

        List<Address> addressList = _signalementBoService.getAddressItem( address );

        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_PROPOSED_ADDRESSES, addressList );
        return getXPage( TEMPLATE_XPAGE_ADRESSE, request.getLocale( ), model );
    }

    /**
     * Validate address.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_VALIDATE_ADDRESS )
    public XPage validateAddress( HttpServletRequest request )
    {
        Adresse adresse = new Adresse( );
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_WITHOUT_JS ) ) && ( request.getParameter( PARAMETER_VALID_ADDRESS ) != null ) )
        {

            String allParameter = request.getParameter( PARAMETER_VALID_ADDRESS );
            String delimiter = "/";

            String [ ] temp = allParameter.split( delimiter );

            // get the address label
            String labelAddress = temp [0].toLowerCase( );

            // get the lat/lng in lambert 27561

            String strLng = temp [1];
            String strLat = temp [2];

            Double dLat = Double.parseDouble( strLat );
            Double dLng = Double.parseDouble( strLng );

            // transform the lambert coordinates to WGS84 for the
            Double [ ] geom = null;

            geom = _signalementBoService.getGeomFromLambertToWgs84( dLng, dLat );

            adresse.setAdresse( labelAddress );
            if ( geom != null )
            {
                adresse.setLng( geom [0] );
                adresse.setLat( geom [1] );
            }
        }
        else
        {
            populate( adresse, request );
        }

        ValidatorLocalisation validateLocalisation = new ValidatorLocalisation( );
        boolean isLocalisationValide = validateLocalisation.validate( adresse );

        Map<String, String> errors = new HashMap<>( );

        if ( !validateLocalisation.isNumeroOk( adresse.getAdresse( ) ) )
        {
            errors.put( PARAMETER_ADDRESS, I18nService.getLocalizedString( MESSAGE_ERROR_NO_STREET_NUMBER, request.getLocale( ) ) );
            request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
            return redirectView( request, VIEW_ADRESSE_ANOMALIE );
        }

        if ( !isLocalisationValide )
        {
            errors.put( PARAMETER_ADDRESS, I18nService.getLocalizedString( MESSAGE_ERROR_LOCALISATION, request.getLocale( ) ) );
            request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
            return redirectView( request, VIEW_ADRESSE_ANOMALIE );
        }

        errors = (Map<String, String>) request.getSession( ).getAttribute( MARK_MAP_ERRORS );
        if ( null != errors )
        {
            errors.remove( PARAMETER_ADDRESS );
        }

        if ( _signalement == null )
        {
            _signalement = new Signalement( );
        }
        _signalement.setAdressesForm( 0, adresse );

        return redirectView( request, VIEW_DOUBLONS );
    }

    /**
     * View doublons.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws UserNotSignedException
     *             the user not signed exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    @View( VIEW_DOUBLONS )
    public XPage viewDoublons( HttpServletRequest request ) throws UserNotSignedException, IOException, JSONException
    {
        if ( ( _signalement == null ) || ( _typeEquipement == null ) )
        {
            return viewAccueil( request );
        }

        Map<String, Object> model = getModel( );

        LuteceUser user = getUser( request, false );
        Adresse adresse = new Adresse( );
        Equipement equipement = new Equipement( );
        List<DossierSignalementDTO> listDoublons;

        // Vérifie si le choix de déclaration était espace public ou équipement
        if ( CHOICE_ESPACE_PUBLIC.equals( _choice ) )
        {
            adresse = _signalement.getAdresses( ).get( 0 );
            double lat = adresse.getLat( );
            double lng = adresse.getLng( );

            listDoublons = getDoublonsAdresses( lat, lng, user );
            // order by distance
            DmrUtils.triBulles( listDoublons );
        }
        else
        {
            equipement = _signalement.getEquipement( );
            listDoublons = getDoublonsEquipement( user, equipement );
        }

        model.put( MARK_LIST_DOUBLONS, listDoublons );

        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_EQUIPEMENT, equipement );
        model.put( MARK_CHOICE, _choice );
        model.put( MARK_TYPE_EQUIPEMENT_LIBELLE, _typeEquipement.getLibelleEcranMobile( ) );
        model.put( MARK_NOT_SIGNED_IN, user == null );
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );

        return getXPage( TEMPLATE_XPAGE_DOUBLONS, request.getLocale( ), model );
    }

    /**
     * Return a listDoublons from the selected adresse coord.
     *
     * @param lat
     *            the lat
     * @param lng
     *            the lng
     * @param user
     *            the user
     * @return listDoublons
     */
    public List<DossierSignalementDTO> getDoublonsAdresses( double lat, double lng, LuteceUser user )
    {

        TypeSignalement typeSignalement = null;

        // get all the dossiers and signalements in perimeter
        List<DossierSignalementDTO> listDoublonsSignalement = _signalementBoService.findAllSignalementInPerimeterWithDTO( lat, lng, 50 );

        // Ajout des dossier Ramen dans la liste des signalement
        List<DossierSignalementDTO> listDossierRamen = _signalementBoService.getDossiersCourrantsByGeomWithLimit( lng, lat );

        Iterator<DossierSignalementDTO> dosIterator = listDossierRamen.iterator( );
        while ( dosIterator.hasNext( ) )
        {
            DossierSignalementDTO dossierSignalementDTO = dosIterator.next( );

            dossierSignalementDTO.setDistance(
                    _signalementBoService.getDistanceBetweenSignalement( lat, lng, dossierSignalementDTO.getLat( ), dossierSignalementDTO.getLng( ) ) );
            typeSignalement = _signalementBoService.findByIdTypeSignalement( 1000 );

            if ( StringUtils.isEmpty( dossierSignalementDTO.getImgUrl( ) ) && ( typeSignalement != null ) )
            {
                dossierSignalementDTO.setImgUrl( AppPropertiesService.getProperty( "signalement-rest.url_picture" ).concat( typeSignalement.getImageUrl( ) ) );
            }
        }
        // set la distance + le type de signalement pour chaque DossierSignalementDTO
        Iterator<DossierSignalementDTO> sigIterator = listDoublonsSignalement.iterator( );
        while ( sigIterator.hasNext( ) )
        {
            DossierSignalementDTO dossierSignalementDTO = sigIterator.next( );
            // Vérification si statut permettant le suivi

            if ( _signalementBoService.isSignalementFollowableAndisSignalementFollowedByUser( dossierSignalementDTO.getId( ).intValue( ),
                    user != null ? user.getName( ) : null, _choice ) )
            {
                sigIterator.remove( );
                continue;
            }

            dossierSignalementDTO.setDistance(
                    _signalementBoService.getDistanceBetweenSignalement( lat, lng, dossierSignalementDTO.getLat( ), dossierSignalementDTO.getLng( ) ) );
            typeSignalement = _signalementBoService.getTypeSignalement( Integer.parseInt( dossierSignalementDTO.getType( ) ), _choice );

            dossierSignalementDTO.setType( typeSignalement.getFormatTypeSignalement( ) );
            // on récupère l'image par defaut s'il n'y a pas d'image enregistrer
            if ( ( dossierSignalementDTO.getImgUrl( ) == null ) || StringUtils.isEmpty( dossierSignalementDTO.getImgUrl( ) ) )
            {
                dossierSignalementDTO
                        .setImgUrl( AppPropertiesService.getProperty( "signalement-rest.url_picture" ).concat( typeSignalement.getRoot( ).getImageUrl( ) ) );
            }
        }
        List<DossierSignalementDTO> listDoublons = listDoublonsSignalement;
        if ( !listDoublons.isEmpty( ) )
        {
            listDoublons.addAll( listDossierRamen );
        }
        else
        {
            listDoublons = listDossierRamen;
        }

        return listDoublons;
    }

    /**
     * Return a listDoublons from the selected equipement.
     *
     * @param user
     *            the user
     * @param equipement
     *            the equipement
     * @return listDoublons
     */
    public List<DossierSignalementDTO> getDoublonsEquipement( LuteceUser user, Equipement equipement )
    {

        TypeSignalement typeSignalement = null;

        // get all the dossiers and signalements in perimeter
        List<DossierSignalementDTO> listDoublonsSignalement = _signalementBoService.getIncidentsByEquipement( equipement.getId( ) );
        // set le type de signalement pour chaque DossierSignalementDTO
        Iterator<DossierSignalementDTO> sigIterator = listDoublonsSignalement.iterator( );
        while ( sigIterator.hasNext( ) )
        {
            DossierSignalementDTO dossierSignalementDTO = sigIterator.next( );
            // Vérification si statut permettant le suivi

            if ( _signalementBoService.isSignalementFollowableAndisSignalementFollowedByUser( dossierSignalementDTO.getId( ).intValue( ),
                    user != null ? user.getName( ) : null, _choice ) )
            {
                sigIterator.remove( );
                continue;
            }

            typeSignalement = _signalementBoService.getTypeSignalement( Integer.parseInt( dossierSignalementDTO.getType( ) ), _choice );

            dossierSignalementDTO.setType( typeSignalement.getFormatTypeSignalement( ) );
            // on récupère l'image par defaut s'il n'y a pas d'image enregistrer
            if ( ( dossierSignalementDTO.getImgUrl( ) == null ) || StringUtils.isEmpty( dossierSignalementDTO.getImgUrl( ) ) )
            {
                dossierSignalementDTO
                        .setImgUrl( AppPropertiesService.getProperty( "equipement-rest.url_picture" ).concat( typeSignalement.getRoot( ).getImageUrl( ) ) );
            }
        }

        return listDoublonsSignalement;
    }

    /**
     * Sets the localisation.
     *
     * @param request
     *            the request
     * @return the x page
     */
    // Valider l'etape Localisation
    @Action( ACTION_VALIDATE_DOUBLONS )
    public XPage setLocalisation( HttpServletRequest request )
    {
        return redirectView( request, VIEW_CATEGORIE );
    }

    /**
     * View type signalement.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws JSONException
     *             the JSON exception
     * @throws UserNotSignedException
     *             the user not signed exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @View( VIEW_CATEGORIE )
    public XPage viewTypeSignalement( HttpServletRequest request ) throws JSONException, UserNotSignedException, IOException
    {

        if ( StringUtils.isEmpty( request.getParameter( PARAMETER_ID_SOURCE ) ) && ( _signalement == null ) )
        {
            return viewAccueil( request );
        }

        Map<String, Object> model = getModel( );
        Adresse adresse = new Adresse( );
        List<TypeSignalementDTO> typeSignalementTree = null;
        Equipement equipement = new Equipement( );

        // Appel depuis une source
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_ID_SOURCE ) ) )
        {
            _bIsFromAnotherSource = true;

            /* Remplissage des étapes précédentes */

            // Step 1 - choix
            _choice = CHOICE_ESPACE_PUBLIC;

            if ( _signalement == null )
            {
                _signalement = new Signalement( );
            }

            // Step2 - Localisation
            try
            {
                double lat = Double.parseDouble( request.getParameter( PARAMETER_POSITION_X ) );
                double lng = Double.parseDouble( request.getParameter( PARAMETER_POSITION_Y ) );
                adresse.setAdresse( _adresseService.getAdresseFromStoreAdr( lat, lng ) );

                _signalement.setAdressesForm( 0, adresse );

                // transform the lambert coordinates to WGS84 for the
                Double [ ] geom = null;
                geom = _signalementBoService.getGeomFromLambert93ToWgs84( lat, lng );

                if ( ( geom != null ) && ( geom.length > 0 ) )
                {
                    adresse.setLat( geom [1] );
                    adresse.setLng( geom [0] );
                }
            }
            catch( Exception e )
            {
                //
            }
            // Récupération des signalements pour la source
            int idSource = Integer.parseInt( request.getParameter( PARAMETER_ID_SOURCE ) );
            typeSignalementTree = _signalementBoService.getTypeSignalementTreeForSource( idSource );

            _source = _signalementBoService.getInfosForSource( idSource );

            _signalement.setCommentaireAgentTerrain( ( _source != null ) && ( _source.getCommentaire( ) != null ) ? _source.getCommentaire( )
                    : request.getParameter( PARAMETER_REF_ITEM_SOURCE ) );
            _strRefItemSource = request.getParameter( PARAMETER_REF_ITEM_SOURCE );
            if ( !StringUtils.isEmpty( _strRefItemSource ) )
            {
                StringBuilder commentaireAgentTerrain = new StringBuilder( );

                if ( !StringUtils.isEmpty( _signalement.getCommentaireAgentTerrain( ) ) )
                {
                    commentaireAgentTerrain.append( _signalement.getCommentaireAgentTerrain( ) + " \n " );
                }

                commentaireAgentTerrain.append( _strRefItemSource );

                _signalement.setCommentaireAgentTerrain( commentaireAgentTerrain.toString( ) );
            }

            _typeEquipement = new TypeEquipement( );
            _typeEquipement.setId( -1L );

        }
        else
            if ( _bIsFromAnotherSource && ( _source != null ) )
            {
                adresse = _signalement.getAdresses( ).get( 0 );

                // Récupération des signalements pour la source
                typeSignalementTree = _signalementBoService.getTypeSignalementTreeForSource( _source.getId( ) );
                _typeEquipement = new TypeEquipement( );
                _typeEquipement.setId( -1L );
            }
            else
            {
                if ( CHOICE_ESPACE_PUBLIC.equals( _choice ) )
                {
                    adresse = _signalement.getAdresses( ).get( 0 );
                    typeSignalementTree = _signalementBoService.getTypeSignalementTree( );
                }
                else
                {
                    equipement = _signalement.getEquipement( );
                    typeSignalementTree = _signalementBoService.getTypeSignalementTreeEquipement( equipement.getParentId( ) );
                }
            }

        // set l'id du type de signalement a 0 - si on récuèpre un id on set l'id pour l'autoséléctionnner
        String id = "0";
        if ( ( _signalement.getTypeSignalement( ) != null ) && ( _signalement.getTypeSignalement( ).getId( ) != null ) )
        {
            id = _signalement.getTypeSignalement( ).getId( ).toString( );
        }

        model.put( VIEW_CATEGORIE, id );
        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_EQUIPEMENT, equipement );
        model.put( MARK_CHOICE, _choice );
        model.put( MARK_TYPE_SIGNALEMENT, typeSignalementTree );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );

        return getXPage( TEMPLATE_XPAGE_CATEGORIE, request.getLocale( ), model );
    }

    /**
     * Sets the type signalment.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_VALIDATE_CATEGORIE )
    public XPage setTypeSignalment( HttpServletRequest request )
    {
        Map<String, String> errors = new HashMap<>( );
        TypeSignalement typeSignalement;
        if ( StringUtils.isNotEmpty( request.getParameter( PARAMETER_WITHOUT_JS ) ) && ( request.getParameter( "radiobtn-grp-acc" ) != null ) )
        {

            typeSignalement = _signalementBoService.getTypeSignalement( Integer.parseInt( request.getParameter( "radiobtn-grp-acc" ) ), _choice );

            _signalement.setTypeSignalement( typeSignalement );
        }
        else
            if ( ( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ) != null )
                    && StringUtils.isNotEmpty( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ) )
                    && !"0".equals( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ) ) )
            {

                typeSignalement = _signalementBoService.getTypeSignalement( Integer.parseInt( request.getParameter( PARAMETER_TYPE_SIGNALEMENT_ID ) ), _choice );

                _signalement.setTypeSignalement( typeSignalement );
            }
            else
            {
                errors.put( PARAMETER_TYPE_SIGNALEMENT, "Veuillez choisir une categorie." );
                return redirectView( request, VIEW_CATEGORIE );
            }

        return redirectView( request, VIEW_FINALISATION );
    }

    /**
     * View finalisation.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws UserNotSignedException
     *             the user not signed exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    @View( VIEW_FINALISATION )
    public XPage viewFinalisation( HttpServletRequest request ) throws UserNotSignedException, IOException, JSONException
    {
        if ( ( _signalement == null ) || ( _typeEquipement == null ) )
        {
            return viewAccueil( request );
        }

        Map<String, Object> model = getModel( );

        List<FileItem> listPhotosDetaillee = new ArrayList<>( );
        List<FileItem> listPhotosEnsemble = new ArrayList<>( );
        Adresse adresse = new Adresse( );
        Equipement equipement = new Equipement( );

        if ( CHOICE_ESPACE_PUBLIC.equals( _choice ) )
        {
            adresse = _signalement.getAdresses( ).get( 0 );
        }
        else
        {
            equipement = _signalement.getEquipement( );
        }

        FileItem photoDetaillee = dansmarueUploadHandler.getFile( request, MARK_PHOTO_DETAILLEE );
        if ( photoDetaillee != null )
        {

            listPhotosDetaillee.add( photoDetaillee );
        }
        FileItem photoEnsemble = dansmarueUploadHandler.getFile( request, MARK_PHOTO_ENSEMBLE );
        if ( photoEnsemble != null )
        {

            listPhotosEnsemble.add( photoEnsemble );
        }

        _listPhotosDetaillee = listPhotosDetaillee;
        _listPhotosEnsemble = listPhotosEnsemble;

        model.put( MARK_LIST_PHOTO_DETAILLEE, _listPhotosDetaillee );
        model.put( MARK_LIST_PHOTO_ENSEMBLE, _listPhotosEnsemble );
        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_EQUIPEMENT, equipement );
        model.put( MARK_CHOICE, _choice );
        model.put( MARK_TYPE_EQUIPEMENT_LIBELLE, _typeEquipement.getLibelleEcranMobile( ) );
        model.put( MARK_TYPE_SIGNALEMENT, _signalement.getTypeSignalement( ).getFormatTypeSignalement( ) );
        // WS
        model.put( dansmarueUploadHandler.getHandlerName( ), dansmarueUploadHandler );

        model.put( MARK_SIGNALEMENT, _signalement );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );

        List<Priorite> priorites = _signalementBoService.getAllPriorite( _choice );

        ReferenceList listePriorite = ListUtils.toReferenceList( priorites, "id", "libelle", null, false );

        if ( _signalement.getPriorite( ) != null )
        {
            for ( ReferenceItem r : listePriorite )
            {

                if ( StringUtils.equals( r.getName( ), _signalement.getPrioriteName( ) ) )
                {
                    r.setChecked( true );
                }
            }
        }
        else
        {
            listePriorite.get( 0 ).setChecked( true );
        }

        model.put( "priorite_list", listePriorite );

        return getXPage( TEMPLATE_XPAGE_FINALISATION, request.getLocale( ), model );
    }

    /**
     * Sets the suivi signalement.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws UserNotSignedException
     *             the user not signed exception
     */
    @Action( ACTION_VALIDATE_SUIVI_SIGNALEMENT )
    public XPage setSuiviSignalement( HttpServletRequest request ) throws UserNotSignedException
    {

        if ( isUserConnected( request ) && StringUtils.isNotEmpty( request.getParameter( PARAMETER_SIGNALEMENT_ID ) )
                && ( request.getParameter( PARAMETER_SIGNALEMENT_ID ) != null ) )
        {
            LuteceUser user = getUser( request, true );
            long idSignalement = Integer.parseInt( request.getParameter( PARAMETER_SIGNALEMENT_ID ) );
            try
            {
                _signalementBoService.addFollower( idSignalement, user.getName( ), "", user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ), "", "", true,
                        _choice );
            }
            catch( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }

        return redirectView( request, VIEW_DOUBLONS );

    }

    /**
     * Sets the finalisation.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_VALIDATE_FINALISATION )
    public XPage setFinalisation( HttpServletRequest request )
    {
        boolean hasError = false;
        Map<String, String> errors = new HashMap<>( );

        // Récupèration de la priorite saisi par l'utilisateur
        ValidatorFinalisation vFinalisation = new ValidatorFinalisation( );
        boolean isFinalisationValide = vFinalisation.validate( request );
        if ( ( request.getParameter( PARAMETER_PRIORITE ) != null )
                && ( !StringUtils.isEmpty( request.getParameter( PARAMETER_PRIORITE ) ) && isFinalisationValide ) )
        {
            Priorite priorite = null;
            try
            {
                priorite = _signalementBoService.loadPrioriteById( NumberUtils.toInt( request.getParameter( PARAMETER_PRIORITE ) ) );
            }
            catch( IOException | JSONException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
            _signalement.setPriorite( priorite );
        }
        else
        {

            errors.put( PARAMETER_PRIORITE, "L'adresse entrée est inexistante ou hors Paris." );
            hasError = true;
        }
        // Récupèration du commentaire
        _signalement.setCommentaire( request.getParameter( PARAMETER_COMMENTAIRE ) );

        // Ajout de la ref item si présente
        if ( !StringUtils.isEmpty( _strRefItemSource ) )
        {
            StringBuilder commentaire = new StringBuilder( );

            // Ajout du commentaire ihm
            if ( !StringUtils.isEmpty( _signalement.getCommentaire( ) ) )
            {
                commentaire.append( _signalement.getCommentaire( ) + " \n " );
            }

            // Ajout du commentaire via la source et/ou l'url
            if ( !StringUtils.isEmpty( _source.getDescription( ) ) )
            {
                commentaire.append( _source.getDescription( ) + " \n " );
            }

            commentaire.append( _strRefItemSource );

            _signalement.setCommentaire( commentaire.toString( ) );
        }

        // supprime les photos s'il existe pour mettre les nouvelles
        if ( !_photos.isEmpty( ) )
        {
            _photos.clear( );

        }
        // Récupèration du fichier
        // WS
        if ( dansmarueUploadHandler.hasFile( request, PARAMETER_PHOTO_DETAILLEE ) )
        {
            Date date = new Date( );
            DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );

            FileItem fileItem = dansmarueUploadHandler.getFile( request, PARAMETER_PHOTO_DETAILLEE );

            PhotoDMR photoDetaillee = new PhotoDMR( );
            photoDetaillee.setVue( PhotoDMR.VUE_DETAILLE );
            photoDetaillee.setDate( dateFormat.format( date ) );
            photoDetaillee.getImageThumbnail( ).setImage( fileItem.get( ) );
            photoDetaillee.getImageThumbnail( ).setMimeType( fileItem.getContentType( ) );
            ImageResource image = new ImageResource( );
            image.setImage( fileItem.get( ) );
            image.setMimeType( fileItem.getContentType( ) );
            photoDetaillee.setImage( image );
            photoDetaillee.setSignalement( _signalement );
            _photos.add( photoDetaillee );

        }
        if ( dansmarueUploadHandler.hasFile( request, PARAMETER_PHOTO_DENSEMBLE ) )
        {
            Date date = new Date( );
            DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );

            FileItem fileItem = dansmarueUploadHandler.getFile( request, PARAMETER_PHOTO_DENSEMBLE );
            PhotoDMR photoDEnsemble = new PhotoDMR( );
            photoDEnsemble.setVue( PhotoDMR.VUE_D_ENSEMBLE );
            photoDEnsemble.setDate( dateFormat.format( date ) );
            photoDEnsemble.getImageThumbnail( ).setImage( fileItem.get( ) );
            photoDEnsemble.getImageThumbnail( ).setMimeType( fileItem.getContentType( ) );
            ImageResource image = new ImageResource( );
            image.setImage( fileItem.get( ) );
            image.setMimeType( fileItem.getContentType( ) );
            photoDEnsemble.setImage( image );
            photoDEnsemble.setSignalement( _signalement );
            _photos.add( photoDEnsemble );
        }
        _signalement.setPhotos( _photos );

        if ( hasError )
        {
            return redirectView( request, VIEW_FINALISATION );
        }

        return redirectView( request, VIEW_SUIVI_SIGNALEMENT );
    }

    /**
     * View suivi signalement.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws UserNotSignedException
     *             the user not signed exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JSONException
     *             the JSON exception
     */
    @View( VIEW_SUIVI_SIGNALEMENT )
    public XPage viewSuiviSignalement( HttpServletRequest request ) throws UserNotSignedException, IOException, JSONException
    {

        if ( ( _signalement == null ) || ( _typeEquipement == null ) )
        {
            return viewAccueil( request );
        }
        Map<String, Object> model = getModel( );
        Adresse adresse = new Adresse( );
        Equipement equipement = new Equipement( );

        if ( CHOICE_ESPACE_PUBLIC.equals( _choice ) )
        {
            adresse = _signalement.getAdresses( ).get( 0 );
        }
        else
        {
            equipement = _signalement.getEquipement( );
        }

        if ( _signaleur == null )
        {
            _signaleur = new Signaleur( );
        }
        FileItem photoDetaillee = dansmarueUploadHandler.getFile( request, MARK_PHOTO_DETAILLEE );
        FileItem photoEnsemble = dansmarueUploadHandler.getFile( request, MARK_PHOTO_ENSEMBLE );

        model.put( MARK_PHOTO_DETAILLEE, photoDetaillee );
        model.put( MARK_PHOTO_ENSEMBLE, photoEnsemble );

        model.put( MARK_ADRESSE, adresse );
        model.put( MARK_EQUIPEMENT, equipement );
        model.put( MARK_CHOICE, _choice );
        model.put( MARK_TYPE_EQUIPEMENT_LIBELLE, _typeEquipement.getLibelleEcranMobile( ) );
        model.put( MARK_TYPE_SIGNALEMENT, _signalement.getTypeSignalement( ).getFormatTypeSignalement( ) );
        model.put( MARK_SIGNALEMENT, _signalement );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        request.getSession( ).setAttribute( MARK_MAP_ERRORS, null );
        model.put( dansmarueUploadHandler.getHandlerName( ), dansmarueUploadHandler );

        LuteceUser user = getUser( request, false );
        if ( isUserConnected( request ) && ( _signaleur.getMail( ) == null ) )
        {
            model.put( MARK_EMAIL, user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ) );
        }
        else
            if ( _signaleur.getMail( ) != null )
            {
                model.put( MARK_EMAIL, _signaleur.getMail( ) );
            }

        model.put( MARK_NOT_SIGNED_IN, user == null );

        return getXPage( TEMPLATE_XPAGE_SUIVI, request.getLocale( ), model );
    }

    /**
     * View confirmation.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @View( VIEW_CONFIRMATION )
    public XPage viewConfirmation( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        return getXPage( TEMPLATE_XPAGE_CONFIRMATION, request.getLocale( ), model );
    }

    /**
     * Sets the signalement.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws UserNotSignedException
     *             the user not signed exception
     */
    @Action( ACTION_VALIDATE_SIGNALEMENT )
    public XPage setSignalement( HttpServletRequest request ) throws UserNotSignedException
    {

        if ( _signalement.getTypeSignalement( ) == null )
        {
            return redirectView( request, AbstractXPage.XPAGE_DANSMARUE );
        }

        LuteceUser user = getUser( request, false );

        if ( ( request.getParameter( PARAMETER_EMAIL ) != null ) && StringUtils.isNotEmpty( PARAMETER_EMAIL ) )
        {

            Signaleur signaleur = new Signaleur( );
            ValidatorSignaleur vsignaleur = new ValidatorSignaleur( );
            Map<String, String> errors = vsignaleur.validate( request );
            if ( !errors.isEmpty( ) )
            {

                return redirectView( request, VIEW_SUIVI_SIGNALEMENT );
            }
            signaleur.setMail( request.getParameter( PARAMETER_EMAIL ) );

            if ( user != null )
            {
                signaleur.setGuid( user.getName( ) );
            }

            List<Signaleur> signaleurs = new ArrayList<>( );
            signaleurs.add( signaleur );
            _signalement.setSignaleurs( signaleurs );

        }
        try
        {
            JSONObject jObject = _signalementBoService.sauvegarderSignalement( _signalement, user, _choice );

            boolean isCreationSignalementOk = jObject.getBoolean( SignalementConstants.RETOUR_CREATION_SIGNALEMENT );

            if ( !isCreationSignalementOk )
            {
                // Erreur à la création de l'ano
                Integer codeErreurCreation = jObject.getInt( SignalementConstants.CODE_ERREUR_CREATION_SIGNALEMENT );
                Map<String, String> errors = new HashMap<>( );

                if ( SignalementConstants.ERREUR_SAUVEGARDE_PHOTO.equals( codeErreurCreation ) )
                {
                    // Erreur à la l'enregistrement des photos
                    errors.put( PARAMETER_SAUVEGARDE_SIGNALEMENT,
                            DatastoreService.getDataValue( "sitelabels.site_property.erreur.enregistrement.photo", null ) );
                    request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
                }
                else
                {
                    // Autres erreurs lors de la création du signalement
                    errors.put( PARAMETER_SAUVEGARDE_SIGNALEMENT, DatastoreService.getDataValue( "sitelabels.site_property.erreur.creation.anomalie", null ) );
                    request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
                }
                return redirectView( request, VIEW_SUIVI_SIGNALEMENT );
            }
        }
        catch( IOException | JSONException e )
        {
            AppLogService.error( e.getMessage( ), e );
            return redirectView( request, VIEW_SUIVI_SIGNALEMENT );
        }
        clearSignalement( request );
        return redirectView( request, VIEW_CONFIRMATION );

    }

    /**
     * Retour.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_RETOUR_ACCUEIL )
    public XPage retour( HttpServletRequest request )
    {
        clearSignalement( request );
        return redirectView( request, AbstractXPage.XPAGE_DANSMARUE );
    }

    /**
     * Download.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_DOWNLOAD )
    public XPage download( HttpServletRequest request )
    {
        // WS
        FileItem fileDemandeItem = dansmarueUploadHandler.getFile( request, request.getParameter( PARAMETER_FIELD_NAME ) );
        if ( fileDemandeItem != null )
        {
            return download( fileDemandeItem.get( ), fileDemandeItem.getName( ), fileDemandeItem.getContentType( ) );
        }
        return null;
    }

    /**
     * Clear signalement.
     *
     * @param request
     *            the request
     */
    private void clearSignalement( HttpServletRequest request )
    {
        _signalement = new Signalement( );
        _equipementList = new ArrayList<>( );
        _typeEquipement = new TypeEquipement( );
        _typeEquipementList = new ArrayList<>( );
        _signaleur = new Signaleur( );
        _photos = new ArrayList<>( );
        _listPhotosEnsemble = new ArrayList<>( );
        _listPhotosDetaillee = new ArrayList<>( );
        _bIsFromAnotherSource = false;
        _strRefItemSource = "";
        _source = null;

        if ( dansmarueUploadHandler.hasFile( request, MARK_PHOTO_DETAILLEE ) )
        {
            dansmarueUploadHandler.removeFileItem( MARK_PHOTO_DETAILLEE, request.getSession( ), 0 );
        }
        if ( dansmarueUploadHandler.hasFile( request, MARK_PHOTO_ENSEMBLE ) )
        {

            dansmarueUploadHandler.removeFileItem( MARK_PHOTO_ENSEMBLE, request.getSession( ), 0 );
        }
    }
}
