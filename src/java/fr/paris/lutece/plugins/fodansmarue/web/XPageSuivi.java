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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import fr.paris.lutece.plugins.fodansmarue.business.dto.HistorySignalementDTO;
import fr.paris.lutece.plugins.fodansmarue.business.entities.Signalement;
import fr.paris.lutece.plugins.fodansmarue.commons.BusinessException;
import fr.paris.lutece.plugins.fodansmarue.utils.constants.SignalementConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

/**
 * The Class XPageSuivi.
 */
@Controller( xpageName = AbstractXPage.XPAGE_SUIVI, pageTitleI18nKey = "fodansmarue.xpage.dansmarue.pageTitle", pagePathI18nKey = "fodansmarue.xpage.dansmarue.pageTitle" )
public class XPageSuivi extends AbstractXPage
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant TEMPLATE_XPAGE_SUIVI_SIGNALEMENT. */
    // TEMPLATES
    private static final String TEMPLATE_XPAGE_SUIVI_SIGNALEMENT = "/skin/plugins/suivi/suivi_signalement.html";

    /** The Constant ACTION_VALIDER_CHECKBOX. */
    // ACTIONS
    private static final String ACTION_VALIDER_CHECKBOX = "validate_checkbox";

    /** The Constant ACTION_VALIDER_SERVICE_FAIT. */
    private static final String ACTION_VALIDER_SERVICE_FAIT = "validate_service_fait";

    /** The Constant MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE. */
    // MESSAGES
    private static final String MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE = "fodansmarue.page.suivi.erreur.signalement.introuvable";

    /** The Constant MESSAGE_ERREUR_SERVICE_FAIT_ERROR. */
    private static final String MESSAGE_ERREUR_SERVICE_FAIT_ERROR = "fodansmarue.page.suivi.erreur.signalement.service.fait";

    /** The Constant MARK_SIGNALEMENT. */
    // MARKER
    private static final String MARK_SIGNALEMENT = "signalement";

    /** The Constant MARK_MESSAGE_ERREUR. */
    private static final String MARK_MESSAGE_ERREUR = "erreur";

    /** The Constant MARK_CURRENT_STATE. */
    private static final String MARK_CURRENT_STATE = "currentState";

    /** The Constant MARK_CURRENT_STATE_ID. */
    private static final String MARK_CURRENT_STATE_ID = "currentStateId";

    /** The Constant MARK_DATE_LAST_STATE. */
    private static final String MARK_DATE_LAST_STATE = "currentStateDate";

    /** The Constant MARK_HISTORY. */
    private static final String MARK_HISTORY = "history";

    /** The Constant MARK_SERVICE_FAIT_AVAILABLE. */
    private static final String MARK_SERVICE_FAIT_AVAILABLE = "service_fait_available";

    /** The Constant MARK_TOKEN. */
    private static final String MARK_TOKEN = "token";

    /** The Constant MARK_INSTANCE. */
    private static final String MARK_INSTANCE = "instance";

    /** The Constant PROPERTY_ID_STATE_SERVICE_FAIT. */
    // PROPERTIES
    public static final String PROPERTY_ID_STATE_SERVICE_FAIT = "signalement.idStateServiceFait";

    /** The Constant PROPERTY_ID_STATE_SERVICE_FAIT_LOCK. */
    public static final String PROPERTY_ID_STATE_SERVICE_FAIT_LOCK = "service.fait.lock";

    /** The Constant PROPERTY_ID_STATE_REPORT_CLOSE. */
    public static final String PROPERTY_ID_STATE_REPORT_CLOSE = "state.close.report";

    /** The Constant STATUT_EN_COURS. */
    private static final String STATUT_EN_COURS = "En cours";

    /** The Constant STATUT_CLOS. */
    private static final String STATUT_CLOS = "Clôturée";

    /**
     * Returns the content of the page accueil.
     *
     * @param request
     *            The HTTP request
     * @return The view
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @View( value = AbstractXPage.XPAGE_SUIVI, defaultView = true )
    public XPage viewAccueil( HttpServletRequest request ) throws IOException
    {
        Map<String, Object> model = getModel( );
        String erreur = "";
        try
        {
            getSignalement( request, model );
        }
        catch( BusinessException e )
        {
            erreur = e.getCode( );
            AppLogService.error( e );
        }

        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        model.put( MARK_MESSAGE_ERREUR, erreur );
        return getXPage( TEMPLATE_XPAGE_SUIVI_SIGNALEMENT, request.getLocale( ), model );
    }

    /**
     * Displayed in accessibility mode, fills the proposed address list.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_VALIDER_CHECKBOX )
    public XPage validerCheckbox( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        return getXPage( TEMPLATE_XPAGE_SUIVI_SIGNALEMENT, request.getLocale( ), model );
    }

    /**
     * Set service fait.
     *
     * @param request
     *            the request
     * @return the x page
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Action( ACTION_VALIDER_SERVICE_FAIT )
    public XPage validerServiceFait( HttpServletRequest request ) throws IOException
    {
        Map<String, Object> model = getModel( );
        String erreur = "";

        String token = request.getParameter( MARK_TOKEN );
        String instance = request.getParameter( MARK_INSTANCE );
        Signalement signalement;

        boolean serviceFait = false;

        // Control validate state
        getSignalement( request, model );
        Integer signalementState = (Integer) model.get( MARK_CURRENT_STATE_ID );
        if ( ( signalementState != null ) && ( signalementState.intValue( ) == SignalementConstants.STATE_FAILURE_SEND_WS ) )
        {
            token = null;
        }

        if ( ( token != null ) && ( instance != null ) )
        {
            serviceFait = _signalementBoService.validateServiceFaitSignalementByToken( token, instance );
        }

        if ( serviceFait )
        {

            try
            {
                getSignalement( request, model );
            }
            catch( BusinessException e )
            {
                erreur = e.getCode( );
                AppLogService.error( e );
            }
        }
        else
        {
            signalement = null;
            throw new BusinessException( signalement, I18nService.getLocalizedString( MESSAGE_ERREUR_SERVICE_FAIT_ERROR, request.getLocale( ) ) );
        }

        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        model.put( MARK_MESSAGE_ERREUR, erreur );
        return getXPage( TEMPLATE_XPAGE_SUIVI_SIGNALEMENT, request.getLocale( ), model );
    }

    /**
     * Gets the signalement.
     *
     * @param request
     *            the request
     * @param model
     *            the model
     * @return the signalement
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void getSignalement( HttpServletRequest request, Map<String, Object> model ) throws IOException
    {

        String token = request.getParameter( MARK_TOKEN );
        String instance = request.getParameter( MARK_INSTANCE );
        String boLink = request.getParameter( "bo_link" );
        String history = "";
        boolean serviceFaitAvailable = false;

        Signalement signalement;
        if ( token != null )
        {
            signalement = _signalementBoService.getSignalementByToken( token, instance );
            if ( signalement == null )
            {
                throw new BusinessException( new Signalement( ),
                        I18nService.getLocalizedString( MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE, request.getLocale( ) ) );
            }

            try
            {
                history = _signalementBoService.getHistorySignalement( signalement.getId( ).intValue( ), instance );
            }
            catch( IOException e )
            {
                AppLogService.error( e );
            }

            if ( StringUtils.isNotBlank( history ) )
            {
                ObjectMapper mapper = new ObjectMapper( ).configure( Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );

                JsonNode json = mapper.readTree( history );

                // List History
                List<HistorySignalementDTO> listHistory = mapper.readValue( json.findPath( MARK_HISTORY ),
                        mapper.getTypeFactory( ).constructCollectionType( List.class, HistorySignalementDTO.class ) );
                // Service fait
                Boolean serviceFait = mapper.readValue( json.findPath( MARK_SERVICE_FAIT_AVAILABLE ), Boolean.class );
                // Current state
                Integer currentStateId = mapper.readValue( json.findPath( MARK_CURRENT_STATE_ID ), Integer.class );
                model.put( MARK_CURRENT_STATE_ID, currentStateId );
                // Date last state
                String strDateLastState = mapper.readValue( json.findPath( MARK_DATE_LAST_STATE ), String.class );

                List<String> listEtatServiceFaitPossible = Arrays
                        .asList( AppPropertiesService.getProperty( PROPERTY_ID_STATE_SERVICE_FAIT_LOCK ).split( "," ) );

                // State for report close
                List<String> liststateclose = Arrays.asList( AppPropertiesService.getProperty( PROPERTY_ID_STATE_REPORT_CLOSE ).split( "," ) );

                // Vérifie si on peut déclarer le service fait, et si on est bien sur la page depuis le lien reçu par le signaleur par mail
                if ( boLink == null )
                {
                    boLink = "false";
                }
                if ( serviceFait && !"true".equals( boLink ) && !listEtatServiceFaitPossible.contains( currentStateId.toString( ) ) )
                {
                    serviceFaitAvailable = true;
                }

                if ( liststateclose.stream( ).anyMatch( str -> str.equals( currentStateId.toString( ) ) ) )
                {
                    // Clôturée
                    model.put( MARK_CURRENT_STATE, STATUT_CLOS );
                }
                else
                {
                    // En cours
                    model.put( MARK_CURRENT_STATE, STATUT_EN_COURS );
                }

                model.put( MARK_DATE_LAST_STATE, strDateLastState );
                model.put( MARK_HISTORY, listHistory );
            }

            model.put( MARK_SERVICE_FAIT_AVAILABLE, serviceFaitAvailable );
            model.put( MARK_SIGNALEMENT, signalement );
            model.put( MARK_TOKEN, token );
            model.put( MARK_INSTANCE, instance );

        }
        else
        {
            signalement = null;
            throw new BusinessException( signalement, I18nService.getLocalizedString( MESSAGE_ERREUR_SIGNALEMENT_INTROUVABLE, request.getLocale( ) ) );
        }
    }

}
