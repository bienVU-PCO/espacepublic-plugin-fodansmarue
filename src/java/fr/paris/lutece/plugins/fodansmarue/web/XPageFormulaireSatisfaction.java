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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.fodansmarue.commons.BusinessException;
import fr.paris.lutece.plugins.fodansmarue.commons.FunctionnalException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * The Class XPageFormulaireSatisfaction.
 */
@Controller( xpageName = AbstractXPage.XPAGE_FORMULAIRE_SATISFACTION, pageTitleI18nKey = "fodansmarue.xpage.dansmarue.pageTitle", pagePathI18nKey = "fodansmarue.xpage.dansmarue.pageTitle" )
public class XPageFormulaireSatisfaction extends AbstractXPage
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant TEMPLATE_XPAGE_SUIVI_SIGNALEMENT. */
    // TEMPLATES
    private static final String TEMPLATE_XPAGE_FORMULAIRE_SATISFACTION = "/skin/plugins/fosignalement/formulaire_satisfaction.html";

    /** The Constant ACTION_VALIDER_CHOIX. */
    private static final String ACTION_VALIDER_CHOIX = "valider_choix";

    /** The Constant MARK_MESSAGE_ERREUR. */
    private static final String MARK_MESSAGE_ERROR= "error";

    /** The Constant MARK_SIGNALEMENT. */
    // MARKER
    private static final String MARK_SIGNALEMENT = "signalement";

    /** The Constant MARK_TOKEN. */
    private static final String MARK_TOKEN = "token";

    /** The Constant MARK_IS_SATISFACTION_FORM_COMPLETE. */
    private static final String MARK_IS_SATISFACTION_FORM_COMPLETE = "isSatisfactionFormComplete";

    /** The Constant MARK_ERROR_SAVE_SATISFACTION_FORM_ANSWER. */
    private static final String MARK_ERROR_SAVE_SATISFACTION_FORM_ANSWER = "errorSave";

    // PARAMETERS
    /** The Constant PARAMETER_REPONSE_FORMULAIRE. */
    private static final String PARAMETER_REPONSE_FORMULAIRE = "reponseFormulaire";

    /** The Constant PARAMETER_COMMENTAIRE_FORMULAIRE. */
    private static final String PARAMETER_COMMENTAIRE_FORMULAIRE = "commentaire";

    //ERRORS MESSAGES
    /** The Constant MESSAGE_ERROR_REPONSE_OBLIGATOIRE. */
    private static final String MESSAGE_ERROR_REPONSE_OBLIGATOIRE = "fodansmarue.formulaire.satisfaction.reponseObligatoireError";

    /** The Constant MESSAGE_ERROR_SAVE. */
    private static final String MESSAGE_ERROR_SAVE = "fodansmarue.formulaire.satisfaction.saveError";

    private String _strToken;

    private boolean _isSatisfactionFormComplete = false;

    /**
     * Returns the content of the page accueil.
     *
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = AbstractXPage.XPAGE_FORMULAIRE_SATISFACTION, defaultView = true )
    public XPage viewFormulaireSatisfaction( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        if ( StringUtils.isEmpty( _strToken ) )
        {
            _strToken = request.getParameter( MARK_TOKEN );
        }

        model.put( MARK_IS_SATISFACTION_FORM_COMPLETE, _isSatisfactionFormComplete );
        model.put( MARK_TOKEN, _strToken );
        model.put( MARK_MAP_ERRORS, request.getSession( ).getAttribute( MARK_MAP_ERRORS ) );
        request.getSession( ).setAttribute( MARK_MAP_ERRORS, null );

        return getXPage( TEMPLATE_XPAGE_FORMULAIRE_SATISFACTION, request.getLocale( ), model );
    }

    /**
     * Save the user's choice.
     *
     * @param request
     *            the request
     * @return the x page
     */
    @Action( ACTION_VALIDER_CHOIX )
    public XPage validerChoix( HttpServletRequest request ) throws JSONException
    {
        String strReponseFormulaire = request.getParameter( PARAMETER_REPONSE_FORMULAIRE );
        String strCommentaire = request.getParameter( PARAMETER_COMMENTAIRE_FORMULAIRE );

        Map<String, String> errors = new HashMap<>( );

        if ( StringUtils.isEmpty( strReponseFormulaire ) )
        {
            errors.put( PARAMETER_REPONSE_FORMULAIRE, I18nService.getLocalizedString( MESSAGE_ERROR_REPONSE_OBLIGATOIRE, request.getLocale( ) ) );
            request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
            _isSatisfactionFormComplete = false;
        } else
        {
            JSONObject response = _signalementBoService.sauvegarderReponsesFormulaireSatisfaction( _strToken, strReponseFormulaire, strCommentaire );

            if ( response.has( "sauvegarderFeedbackOk" ) )
            {
                String sauvegarderFeedbackOk = response.get( "sauvegarderFeedbackOk" ).toString( );

                boolean isSauvegarderFeedbackOk = Boolean.parseBoolean( sauvegarderFeedbackOk );

                if ( isSauvegarderFeedbackOk )
                {
                    _isSatisfactionFormComplete = true;
                } else
                {
                    errors.put( MARK_ERROR_SAVE_SATISFACTION_FORM_ANSWER, I18nService.getLocalizedString( MESSAGE_ERROR_SAVE, request.getLocale( ) ) );
                    request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
                    _isSatisfactionFormComplete = false;
                }

            }
            else
            {
                errors.put( MARK_ERROR_SAVE_SATISFACTION_FORM_ANSWER, I18nService.getLocalizedString( MESSAGE_ERROR_SAVE, request.getLocale( ) ) );
                request.getSession( ).setAttribute( MARK_MAP_ERRORS, errors );
                _isSatisfactionFormComplete = false;
            }
        }
        return redirectView( request, TEMPLATE_XPAGE_FORMULAIRE_SATISFACTION );

    }
}
